document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("searchName")
    .addEventListener("input", () => fetchSchedules());
  document
    .getElementById("searchMitra")
    .addEventListener("input", () => fetchSchedules());

  async function fetchSchedules(page = 0) {
    const searchName = document.getElementById("searchName").value;
    const searchMitra = document.getElementById("searchMitra").value;

    const response = await fetch(
      `/admin/talent/api?page=${page}&searchName=${searchName}&searchMitra=${searchMitra}`
    );

    if (response.ok) {
      const data = await response.json();
      updateTable(data);
      updatePagination(data);
    } else {
      console.error("Error fetching data", response.status);
    }
  }

  const updateTable = (data) => {
    const tbody = document.getElementById("talent-body");
    tbody.innerHTML = "";

    const itemsPerPage = data.itemsPerPage || 10;
    const currentPage = data.currentPage || 0;
    const startNumber = currentPage * itemsPerPage;

    data.talents.forEach((talent, index) => {
      const rowNumber = startNumber + index + 1;

      const row = document.createElement("tr");
      row.style.textAlign = "center";
      row.innerHTML = `
      <td>${rowNumber}</td>
      <td>${talent.name || ""}</td>
      <td>${talent.email || ""}</td>
      <td>${talent.instituteName || ""}</td>
      <td>
        <a class="btn btn-primary">
          <i class="bi bi-file-earmark-text-fill text-white"></i>
        </a>
        <a href="/admin/talent/detail/${talent.id}" class="btn btn-secondary">
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
    prevLink.innerHTML = "&lt;";
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
    nextLink.innerHTML = "&gt;";
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
