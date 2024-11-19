document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("searchParameterName")
    .addEventListener("input", () => fetchSchedules());
  document
    .getElementById("searchParameterValue")
    .addEventListener("input", () => fetchSchedules());

  async function fetchSchedules(page = 0) {
    const searchParameterName = document.getElementById(
      "searchParameterName"
    ).value;
    const searchParameterValue = document.getElementById(
      "searchParameterValue"
    ).value;

    const response = await fetch(
      `/admin/parameter/api?page=${page}&searchParameterName=${searchParameterName}&searchParameterValue=${searchParameterValue}`
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
    const tbody = document.getElementById("parameter-body");
    tbody.innerHTML = "";

    const itemsPerPage = data.itemsPerPage || 10;
    const currentPage = data.currentPage || 0;
    const startNumber = currentPage * itemsPerPage;

    data.parameters.forEach((parameter, index) => {
      const rowNumber = startNumber + index + 1;

      const row = document.createElement("tr");
      row.innerHTML = `
      <td>${rowNumber}</td>
      <td>${parameter.reference_group1 || ""}</td>
      <td>${parameter.reference_group2 || ""}</td>
      <td>${parameter.reference_name || ""}</td>
      <td>
        <a href="/admin/parameter/update/${
          parameter.id
        }" class="btn btn-primary">
          <i class="bi bi-pencil-square text-white"></i>
        </a>
        <a class="btn btn-danger">
          <i class="bi bi-trash3-fill text-white"></i>
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
