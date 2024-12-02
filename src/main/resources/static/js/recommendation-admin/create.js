$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var position = $("#position").val();
  var selectRtor = $("#selectRtor").val();
  var selectInstitute = $("#selectInstitute").val();
  var description = $("#description").val();

  if (!position || !selectRtor || !selectInstitute || !description) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    if (!position) $("#position").addClass("is-invalid");
    if (!selectRtor) $("#selectRtor").addClass("is-invalid");
    if (!selectInstitute) $("#selectInstitute").addClass("is-invalid");
    if (!description) $("#description").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    vacancyId: selectRtor,
    description: description,
    applicantIds: selectedTalents,
  });

  Swal.fire({
    title: "Apakah Anda yakin ingin membuat 'Rekomendasi Talent' ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      create(data);
    }
  });
}

function create(data) {
  $.ajax({
    url: `/admin/recommendation/create`,
    method: "POST",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Talent baru berhasil direkomendasikan",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/recommendation";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Talent baru gagal direkomendasikan",
        showConfirmButton: true,
      });
    },
  });
}

function confirmBack() {
  Swal.fire({
    title: "Apakah Anda yakin ingin kembali?",
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed) {
      window.location.href = "/admin/recommendation";
    }
  });
  return false;
}

let selectedTalents = [];

async function fetchTalents(id) {
  try {
    const response = await fetch(`/admin/recommendation/talent/${id}`);
    const data = await response.json();
    if (data.length === 0) {
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Data talent tidak ditemukan!",
      });
      return;
    }
    populateTalentTable(data);
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: error.message,
    });
  }
}

function populateTalentTable(data) {
  const tbody = $("#talentTable tbody");
  tbody.empty();

  data.forEach((applicant) => {
    const row = `
      <tr style="text-align: center">
        <td>${applicant.talent.name}</td>
        <td>
          <input type="checkbox" class="select-talent" data-id="${applicant.id}" data-name="${applicant.talent.name}" data-email="${applicant.talent.email}">
        </td>
      </tr>
    `;
    tbody.append(row);
  });
}

$("#addSelectedTalents").on("click", () => {
  const selected = [];
  $(".select-talent:checked").each(function () {
    const id = $(this).data("id");
    const name = $(this).data("name");
    const email = $(this).data("email");

    if (!selectedTalents.find((talent) => talent.id === id)) {
      selectedTalents.push({ id, name, email });
      selected.push({ id, name, email });
    }
  });

  if (selected.length === 0) {
    Swal.fire({
      icon: "warning",
      title: "Peringatan",
      text: "Tidak ada talent yang dipilih!",
    });
    return;
  }

  updateRecommendationTable(selected);

  $("#addTalentModal").modal("hide");
});

$("#addTalentModal").on("hidden.bs.modal", () => {
  const tbody = $("#talentTable tbody");
  tbody.empty();
});

function updateRecommendationTable(newTalents) {
  const tbody = $("#recommendationTbl");
  newTalents.forEach((talent, index) => {
    const row = `
      <tr id="talent-${talent.id}">
        <td>${selectedTalents.length - newTalents.length + index + 1}</td>
        <td>${talent.name}</td>
        <td>${talent.email}</td>
        <td>
          <a href="#" class="btn btn-danger" onclick="deleteTalent('${
            talent.id
          }')">
            <i class="bi bi-trash3-fill text-white"></i>
          </a>
        </td>
      </tr>
    `;
    tbody.append(row);
  });
}

function deleteTalent(id) {
  selectedTalents = selectedTalents.filter((talent) => talent.id !== id);
  $(`#talent-${id}`).remove();
}

$("#addTalentButton").on("click", () => {
  var selectRtor = $("#selectRtor").val();

  if (!selectRtor) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Kolom rtor harus diisi terlebih dahulu",
    });
  } else {
    fetchTalents(selectRtor);
    $("#addTalentModal").modal("show");
  }
});
