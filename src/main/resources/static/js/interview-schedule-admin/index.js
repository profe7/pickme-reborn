document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("searchRecruiter")
    .addEventListener("input", () => fetchSchedules());
  document
    .getElementById("searchTalent")
    .addEventListener("input", () => fetchSchedules());
  document
    .getElementById("type")
    .addEventListener("change", () => fetchSchedules());
  document
    .getElementById("date")
    .addEventListener("input", () => fetchSchedules());
  document
    .getElementById("status")
    .addEventListener("change", () => fetchSchedules());

  async function fetchSchedules(page = 0) {
    const searchRecruiter = document.getElementById("searchRecruiter").value;
    const searchTalent = document.getElementById("searchTalent").value;
    const type = document.getElementById("type").value;
    const date = document.getElementById("date").value;
    const status = document.getElementById("status").value;

    const response = await fetch(
      `/admin/interview-schedule/api?page=${page}&searchRecruiter=${searchRecruiter}&searchTalent=${searchTalent}&type=${type}&date=${date}&status=${status}`
    );

    if (response.ok) {
      const data = await response.json();
      updateTable(data);
      updatePagination(data);
    } else {
      console.error("Error fetching data", response.status);
    }
  }

  function formatTime(timeString) {
    const time = timeString || "00:00";
    return time;
  }

  const statusColors = {
    ON_PROCESS: "#0000FF", // biru
    RESCHEDULED: "#808080", // abu-abu
    CANCELLED: "#FFFF00", // kuning
    ACCEPTED: "#008000", // hijau
    REJECTED: "#FF0000", // merah
    INACTIVE: "#808080", // abu-abu
  };

  const updateTable = (data) => {
    const tbody = document.getElementById("schedule-body");
    tbody.innerHTML = "";

    const itemsPerPage = data.itemsPerPage || 10;
    const currentPage = data.currentPage || 0;
    const startNumber = currentPage * itemsPerPage;

    data.interviews.forEach((interview, index) => {
      const rowNumber = startNumber + index + 1;

      const status = interview.status
        ? interview.status
        : { name: "Status Tidak Tersedia", color: "#ccc" };

      const statusColor = statusColors[status] || "#ccc";

      const formattedDate = interview.date
        ? new Date(interview.date).toLocaleDateString("en-CA")
        : "";

      const formattedStartTime = interview.startTime
        ? formatTime(interview.startTime)
        : "";

      const formattedEndTime = interview.endTime
        ? formatTime(interview.endTime)
        : "";

      const timeRange =
        formattedStartTime !== "" && formattedEndTime !== ""
          ? `${formattedStartTime} - ${formattedEndTime}`
          : "";

      const row = document.createElement("tr");
      row.style.textAlign = "center";
      row.innerHTML = `
      <td>${rowNumber}</td>
      <td><a href="/admin/interview-schedule/history/${interview.id}">${
        interview.clientUserFirstName || ""
      }</a></td>
      <td>${interview.applicantTalentName || ""}</td>
      <td>${interview.position || ""}</td>
      <td>${interview.interviewType || ""}</td>
      <td>${formattedDate}</td>
      <td>${timeRange}</td>
      <td><span class="badge" style="background-color:${statusColor}">${status}</span></td>
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
