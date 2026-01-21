const API_USERS = "/api/admin/users";
const API_ROLES = "/api/admin/roles";

const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

function rolesToText(roles) {
    return (roles || []).map(r => (r.name || "").replace("ROLE_", "")).join(" ");
}

async function apiFetch(url, options = {}) {
    const opts = { credentials: "same-origin", ...options };
    opts.headers = { ...(opts.headers || {}) };

    const method = (opts.method || "GET").toUpperCase();
    if (["POST", "PUT", "DELETE", "PATCH"].includes(method)) {
        if (csrfToken && csrfHeader) opts.headers[csrfHeader] = csrfToken;
    }

    if (opts.body && !opts.headers["Content-Type"]) {
        opts.headers["Content-Type"] = "application/json";
    }

    const res = await fetch(url, opts);
    if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(text || `HTTP ${res.status}`);
    }
    if (res.status === 204) return null;
    return res.json();
}

async function loadRolesToSelect(selectEl) {
    const roles = await apiFetch(API_ROLES);
    selectEl.innerHTML = "";
    roles.forEach(r => {
        const opt = document.createElement("option");
        opt.value = r.id;
        opt.textContent = (r.name || "").replace("ROLE_", "");
        selectEl.appendChild(opt);
    });
}

function setSelectedRoles(selectEl, roleIds) {
    const ids = new Set((roleIds || []).map(Number));
    Array.from(selectEl.options).forEach(o => {
        o.selected = ids.has(Number(o.value));
    });
}

function getSelectedRoleIds(selectEl) {
    return Array.from(selectEl.selectedOptions).map(o => Number(o.value));
}

const tbody = document.getElementById("usersTbody");

function renderUsers(users) {
    tbody.innerHTML = "";
    users.forEach(u => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td>${u.id}</td>
      <td>${escapeHtml(u.name ?? "")}</td>
      <td>${escapeHtml(u.surname ?? "")}</td>
      <td>${u.age ?? ""}</td>
      <td>${escapeHtml(u.username ?? "")}</td>
      <td>${escapeHtml(rolesToText(u.roles))}</td>
      <td>
        <button type="button" class="btn btn-info btn-sm js-edit" data-id="${u.id}">Edit</button>
      </td>
      <td>
        <button type="button" class="btn btn-danger btn-sm js-del" data-id="${u.id}">Delete</button>
      </td>
    `;
        tbody.appendChild(tr);
    });
}

async function refreshUsers() {
    const users = await apiFetch(API_USERS);
    renderUsers(users);
}

const createForm = document.getElementById("createForm");
const createRoles = document.getElementById("createRoles");
const createErrors = document.getElementById("createErrors");

createForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    createErrors.textContent = "";

    const fd = new FormData(createForm);
    const dto = {
        username: (fd.get("username") || "").trim(),
        password: fd.get("password") || "",
        name: (fd.get("name") || "").trim(),
        surname: (fd.get("surname") || "").trim(),
        age: fd.get("age") ? Number(fd.get("age")) : null,
        roleIds: getSelectedRoleIds(createRoles)
    };

    try {
        await apiFetch(API_USERS, { method: "POST", body: JSON.stringify(dto) });
        createForm.reset();

        $('#users-tab').tab('show');
        await refreshUsers();
    } catch (err) {
        createErrors.textContent = err.message;
    }
});

const editForm = document.getElementById("editForm");
const editRoles = document.getElementById("editRoles");
const editErrors = document.getElementById("editErrors");

tbody.addEventListener("click", async (e) => {
    const editBtn = e.target.closest(".js-edit");
    const delBtn = e.target.closest(".js-del");
    const id = editBtn?.dataset.id || delBtn?.dataset.id;
    if (!id) return;

    try {
        const user = await apiFetch(`${API_USERS}/${id}`);

        if (editBtn) {
            editErrors.textContent = "";
            editForm.elements["id"].value = user.id;
            editForm.elements["name"].value = user.name ?? "";
            editForm.elements["surname"].value = user.surname ?? "";
            editForm.elements["age"].value = user.age ?? "";
            editForm.elements["username"].value = user.username ?? "";
            editForm.elements["password"].value = ""; // не показываем
            setSelectedRoles(editRoles, (user.roles || []).map(r => r.id));

            $("#editModal").modal("show");
        }

        if (delBtn) {
            fillDeleteModal(user);
            $("#deleteModal").modal("show");
        }
    } catch (err) {
        alert(err.message);
    }
});

editForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    editErrors.textContent = "";

    const fd = new FormData(editForm);
    const id = Number(fd.get("id"));

    const dto = {
        id,
        username: (fd.get("username") || "").trim(),
        password: fd.get("password") || "", // пусто => твой сервис оставит старый
        name: (fd.get("name") || "").trim(),
        surname: (fd.get("surname") || "").trim(),
        age: fd.get("age") ? Number(fd.get("age")) : null,
        roleIds: getSelectedRoleIds(editRoles)
    };

    try {
        await apiFetch(`${API_USERS}/${id}`, { method: "PUT", body: JSON.stringify(dto) });
        $("#editModal").modal("hide");
        await refreshUsers();
    } catch (err) {
        editErrors.textContent = err.message;
    }
});

const deleteForm = document.getElementById("deleteForm");
const deleteErrors = document.getElementById("deleteErrors");

function fillDeleteModal(user) {
    deleteErrors.textContent = "";
    deleteForm.elements["id"].value = user.id;
    deleteForm.elements["name"].value = user.name ?? "";
    deleteForm.elements["surname"].value = user.surname ?? "";
    deleteForm.elements["age"].value = user.age ?? "";
    deleteForm.elements["username"].value = user.username ?? "";
    deleteForm.elements["roles"].value = rolesToText(user.roles);
}

deleteForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    deleteErrors.textContent = "";

    const id = Number(deleteForm.elements["id"].value);

    try {
        await apiFetch(`${API_USERS}/${id}`, { method: "DELETE" });
        $("#deleteModal").modal("hide");
        await refreshUsers();
    } catch (err) {
        deleteErrors.textContent = err.message;
    }
});

function escapeHtml(str) {
    return String(str)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

(async function init() {
    await loadRolesToSelect(createRoles);
    await loadRolesToSelect(editRoles);
    await refreshUsers();
})();
