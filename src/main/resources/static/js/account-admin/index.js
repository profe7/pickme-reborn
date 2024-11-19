const accountStatuses = ["AKTIF", "TIDAK AKTIF"];

document.addEventListener("DOMContentLoaded", function () {
  const statusSelect = document.getElementById("status");
  accountStatuses.forEach(function (status) {
    const option = document.createElement("option");
    option.value = status;
    option.textContent = status;
    statusSelect.appendChild(option);
  });
  document
    .getElementById("searchUsername")
    .addEventListener("input", () => fetchSchedules());
  document
    .getElementById("role")
    .addEventListener("input", () => fetchSchedules());
  document
    .getElementById("status")
    .addEventListener("change", () => fetchSchedules());

  async function fetchSchedules(page = 0) {
    const searchUsername = document.getElementById("searchUsername").value;
    const role = document.getElementById("role").value;
    const status = document.getElementById("status").value;

    const response = await fetch(
      `/admin/account/api?page=${page}&searchUsername=${searchUsername}&role=${role}&status=${status}`
    );

    if (response.ok) {
      const data = await response.json();
      updateTable(data);
      updatePagination(data);
    } else {
      console.error("Error fetching data", response.status);
    }
  }

  const statusColors = {
    AKTIF: "#008000", // abu-abu
    "TIDAK AKTIF": "#FF0000", // merah
  };

  const updateTable = (data) => {
    const tbody = document.getElementById("account-body");
    tbody.innerHTML = "";

    const itemsPerPage = data.itemsPerPage || 10;
    const currentPage = data.currentPage || 0;
    const startNumber = currentPage * itemsPerPage;

    data.accounts.forEach((account, index) => {
      const rowNumber = startNumber + index + 1;

      const status = account.status ? "AKTIF" : "TIDAK AKTIF";

      const statusColor = statusColors[status] || "#ccc";

      const row = document.createElement("tr");
      row.innerHTML = `
      <td>${rowNumber}</td>
      <td>${account.username || ""}</td>
      <td>${account.email || ""}</td>
      <td>${account.instituteName || ""}</td>
      <td>${account.roleName || ""}</td>
      <td><input type="checkbox" ${
        account.access ? "checked" : ""
      } onClick="" style="transform: scale(1.5); margin: 5px;"></td>
      <td><span class="badge" style="background-color:${statusColor}">${status}</span></td>
      <td>
        <a class="btn btn-primary">
          <i class="bi bi-pencil-square text-white"></i>
        </a>
        <a href="/admin/account/${account.id}" class="btn btn-secondary">
          <i class="bi bi-info-circle-fill text-white"></i>
        </a>
      </td>
    `;
      tbody.appendChild(row);
    });
  };

  const updatePagination = (data) => {
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";
    const currentPage = data.currentPage ?? 0;
    const totalPages = data.totalPages ?? 1;
    const prevButton = document.createElement("li");
    prevButton.classList.add("page-item");
    prevButton.classList.toggle("disabled", currentPage <= 0);
    const prevLink = document.createElement("a");
    prevLink.classList.add("page-link");
    prevLink.textContent = "Prev";
    prevLink.href = "#";
    prevLink.addEventListener("click", (e) => {
      e.preventDefault();
      if (currentPage > 0) fetchSchedules(currentPage - 1);
    });
    prevButton.appendChild(prevLink);
    pagination.appendChild(prevButton);

    for (let i = 0; i < totalPages; i++) {
      const pageButton = document.createElement("li");
      pageButton.classList.add("page-item");
      pageButton.classList.toggle("active", i === currentPage);
      const pageLink = document.createElement("a");
      pageLink.classList.add("page-link");
      pageLink.textContent = i + 1;
      pageLink.href = "#";
      pageLink.addEventListener("click", (e) => {
        e.preventDefault();
        fetchSchedules(i);
      });
      pageButton.appendChild(pageLink);
      pagination.appendChild(pageButton);
    }

    const nextButton = document.createElement("li");
    nextButton.classList.add("page-item");
    nextButton.classList.toggle("disabled", currentPage >= totalPages - 1);
    const nextLink = document.createElement("a");
    nextLink.classList.add("page-link");
    nextLink.textContent = "Next";
    nextLink.href = "#";
    nextLink.addEventListener("click", (e) => {
      e.preventDefault();
      if (currentPage < totalPages - 1) fetchSchedules(currentPage + 1);
    });
    nextButton.appendChild(nextLink);
    pagination.appendChild(nextButton);

    document.getElementById("paginationInfo").textContent = `Page ${
      currentPage + 1
    } of ${totalPages}`;
  };

  fetchSchedules();
});
