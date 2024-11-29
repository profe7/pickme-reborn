document.addEventListener("DOMContentLoaded", () => {
  document
    .getElementById("searchName")
    .addEventListener("input", () => fetchApplied());
  document
    .getElementById("searchPosition")
    .addEventListener("change", () => fetchApplied());
  document
    .getElementById("searchSkill")
    .addEventListener("change", () => fetchApplied());

  async function fetchApplied(page = 0) {
    const searchName = document.getElementById("searchName").value;
    const searchPosition = document.getElementById("searchPosition").value;
    const searchSkill = document.getElementById("searchSkill").value;
    const recId = document.getElementById("recId").value;

    const response = await fetch(
      `/admin/recommendation/detail/talent/${recId}?page=${page}&searchName=${searchName}&searchPosition=${searchPosition}&searchSkill=${searchSkill}`
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
    const container = document.getElementById("detailTbl");
    container.innerHTML = "";
    const row = document.createElement("div");
    row.classList.add("row");

    if (data.talents && data.talents.length > 0) {
      data.talents.forEach((talent) => {
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
        profileImg.src = talent.applicantPhoto || "/img/no_image.jpg";
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
        title.textContent = talent.applicantName || "-";

        const text = document.createElement("p");
        text.classList.add("card-text", "mb-1");
        text.textContent = talent.applicantMitra || "-";

        const skillsContainer = document.createElement("div");
        const skillsContainerH5 = document.createElement("h5");
        skillsContainer.appendChild(skillsContainerH5);
        if (talent.applicantSkill && talent.applicantSkill.length > 0) {
          talent.applicantSkill.forEach((skill) => {
            const skillBadge = document.createElement("span");
            skillBadge.classList.add(
              "badge",
              "text-bg-primary",
              "me-2",
              "mb-1"
            );
            skillBadge.textContent = skill.name;
            skillBadge.style.fontWeight = "100";
            skillsContainerH5.appendChild(skillBadge);
          });
        }

        textContainer.appendChild(title);
        textContainer.appendChild(text);
        textContainer.appendChild(skillsContainer);

        const checkboxContainer = document.createElement("div");
        checkboxContainer.classList.add("d-flex", "ms-auto");
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.id = talent.applicantId;
        checkbox.value = talent.applicantName;
        checkbox.addEventListener("change", (e) => toggleSelected(e));
        checkbox.style.transform = "scale(1.5)";
        checkboxContainer.appendChild(checkbox);

        card.addEventListener("click", () => {
          viewTalentDetail(talent.applicantId);
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

  window.viewTalentDetail = (talentId) => {
    const iframe = document.getElementById("talentDetail");
    const noDataDiv = document.getElementById("talentDetailNoData");

    if (talentId) {
      iframe.style.display = "block";
      noDataDiv.style.display = "none";
      iframe.src = `/admin/share/${talentId}`;
    } else {
      iframe.style.display = "none";
      noDataDiv.style.display = "block";
    }
  };

  fetchApplied();
});
