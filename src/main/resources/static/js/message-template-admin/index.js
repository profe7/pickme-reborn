document.addEventListener("DOMContentLoaded", function () {
  async function fetchSchedules(page = 0) {
    const response = await fetch(`/admin/message-template/api?page=${page}`);

    if (response.ok) {
      const data = await response.json();
      updateTable(data);
      updatePagination(data);
    } else {
      console.error("Error fetching data", response.status);
    }
  }

  const updateTable = (data) => {
    const tbody = document.getElementById("message-template-body");
    tbody.innerHTML = "";

    const itemsPerPage = data.itemsPerPage || 10;
    const currentPage = data.currentPage || 0;
    const startNumber = currentPage * itemsPerPage;

    data.messages.forEach((message, index) => {
      const rowNumber = startNumber + index + 1;

      const row = document.createElement("tr");
      row.style.textAlign = "center";
      row.innerHTML = `
      <td>${rowNumber}</td>
      <td>${message.message || ""}</td>
      <td>${message.type || ""}</td>
      <td>
        <a href="/admin/message-template/update/${
          message.id
        }" class="btn btn-success">
          <i class="bi bi-pencil-square text-white"></i>
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
