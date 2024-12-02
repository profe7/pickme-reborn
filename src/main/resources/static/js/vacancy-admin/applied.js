document.addEventListener("DOMContentLoaded", () => {
  document
    .getElementById("mitra")
    .addEventListener("input", () => fetchApplied());

  async function fetchApplied(page = 0) {
    const searchInstitute = document.getElementById("mitra").value;
    const jobId = document.getElementById("jobId").value;

    const response = await fetch(
      `/admin/vacancy/applied/applicant/${jobId}?page=${page}&searchInstitute=${searchInstitute}`
    );

    if (response.ok) {
      const data = await response.json();
      updateTable(data);
      updatePagination(data);
    } else {
      console.error("Error fetching data", response.status);
    }
  }

  const selectedApplicants = new Set();

  function toggleSelected(event) {
    const checkbox = event.target;
    const applicantId = checkbox.id;

    if (checkbox.checked) {
      selectedApplicants.add(applicantId);
    } else {
      selectedApplicants.delete(applicantId);
    }
    toggleSubmitButton();
  }

  function toggleSubmitButton() {
    const submitButton = document.getElementById("submit-button");
    if (selectedApplicants.size > 0) {
      submitButton.disabled = false;
    } else {
      submitButton.disabled = true;
    }
  }

  const updateTable = (data) => {
    const container = document.getElementById("appliedTbl");
    container.innerHTML = "";
    const row = document.createElement("div");
    row.classList.add("row");

    if (data.applicants && data.applicants.length > 0) {
      data.applicants.forEach((applicant) => {
        const card = document.createElement("div");
        card.classList.add("card", "w-100", "shadow-none", "border");

        const cardBody = document.createElement("div");
        cardBody.classList.add(
          "card-body",
          "d-flex",
          "justify-content-between",
          "align-items-center"
        );

        const imgContainer = document.createElement("div");
        imgContainer.classList.add("d-flex", "align-items-center");
        const profileImg = document.createElement("img");
        profileImg.src = applicant.applicantPhoto || "/img/no_image.jpg";
        profileImg.alt = "Profile Image";
        profileImg.classList.add("rounded-circle", "me-3");
        profileImg.style.width = "40px";
        profileImg.style.height = "40px";
        profileImg.style.objectFit = "cover";
        profileImg.style.borderRadius = "8px";
        imgContainer.appendChild(profileImg);

        const textContainer = document.createElement("div");
        const title = document.createElement("h5");
        title.classList.add("card-title", "fw-bold");
        title.textContent = applicant.applicantName || "-";

        const text = document.createElement("p");
        text.classList.add("card-text");
        text.textContent = applicant.applicantMitra || "-";

        textContainer.appendChild(title);
        textContainer.appendChild(text);

        const checkboxContainer = document.createElement("div");
        checkboxContainer.classList.add("d-flex", "ms-auto");
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.id = applicant.applicantId;
        checkbox.value = applicant.applicantName;
        checkbox.addEventListener("change", (e) => toggleSelected(e));
        checkbox.style.transform = "scale(1.5)";
        checkboxContainer.appendChild(checkbox);

        card.addEventListener("click", () => {
          viewTalentDetail(applicant.applicantId);
        });

        cardBody.appendChild(imgContainer);
        cardBody.appendChild(textContainer);
        cardBody.appendChild(checkboxContainer);
        card.appendChild(cardBody);

        row.appendChild(card);
      });

      container.appendChild(row);
    } else {
      container.innerHTML = `
      <div class="col text-center">
        <h5>Tidak ada data pelamar</h5>
      </div>
    `;
    }
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
      if (currentPage > 0) fetchApplied(currentPage - 1);
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
        fetchApplied(i);
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
      if (currentPage < totalPages - 1) fetchApplied(currentPage + 1);
    });
    nextButton.appendChild(nextLink);
    pagination.appendChild(nextButton);

    document.getElementById("paginationInfo").textContent = `Page ${
      currentPage + 1
    } of ${totalPages}`;
  };

  const submitButton = document.getElementById("submit-button");
  submitButton.addEventListener("click", () => {
    if (!submitButton.disabled) {
      applied(data);
    }
  });

  var data = JSON.stringify({
    applicantIds: Array.from(selectedApplicants),
    description: null,
    position: null,
    vacancyId: document.getElementById("jobId").value,
  });

  function applied(data) {
    $.ajax({
      url: `/admin/vacancy/applied`,
      method: "POST",
      dataType: "JSON",
      contentType: "application/json",
      data: data,
      success: (result) => {
        $.LoadingOverlay("hide");
        Swal.fire({
          position: "center",
          icon: "success",
          title: "Talent berhasil direkomendasikan",
          showConfirmButton: true,
        }).then(() => {
          window.location.href = "/admin/vacancy";
        });
      },
      error: (e) => {
        $.LoadingOverlay("hide");
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Talent gagal direkomendasikan",
          showConfirmButton: true,
        });
      },
    });
  }

  window.viewTalentDetail = (applicantId) => {
    const iframe = document.getElementById("talentDetail");
    const noDataDiv = document.getElementById("talentDetailNoData");

    if (applicantId) {
      iframe.style.display = "block";
      noDataDiv.style.display = "none";
      iframe.src = `/admin/share/${applicantId}`;
    } else {
      iframe.style.display = "none";
      noDataDiv.style.display = "block";
    }
  };

  fetchApplied();
});
