$(document).ready(function () {
  $(".select2").select2();

  $("#selectIsOffline").change(function () {
    var selectedValue = $(this).val();

    if (selectedValue === "OFFLINE") {
      $("#locationAddress").prop("disabled", false).val("");
      $("#interviewLink").prop("disabled", true).val("");
    } else if (selectedValue === "ONLINE") {
      $("#interviewLink").prop("disabled", false).val("");
      $("#locationAddress").prop("disabled", true).val("");
    }
  });

  $("#position").change(function () {
    fetchTalents();
  });
});

async function fetchTalents() {
  const position = $("#position").val();

  // Cek jika posisi tidak ada
  if (!position) {
    $("#talentId").html(
      '<option value="" disabled selected>Pilih Talent</option>'
    );
    $("#talentId").prop("disabled", true);
    $("#recruiterId").html(
      '<option value="" disabled selected>Pilih Perekrut/Klien</option>'
    );
    $("#recruiterId").prop("disabled", true);
    return;
  }

  try {
    const applicantResponse = await fetch(
      `/admin/interview-schedule/create/talent?position=${position}`
    );
    const response = await applicantResponse.json();

    let talentOptions =
      '<option value="" disabled selected>Pilih Talent</option>';
    let recruiterOptions =
      '<option value="" disabled selected>Pilih Perekrut/Klien</option>';
    response.applicants.forEach((applicant) => {
      talentOptions += `<option value="${applicant.applicantId}">${applicant.talentName}</option>`;
      recruiterOptions += `<option value="${applicant.clientId}">${applicant.clientName}</option>`;
    });
    $("#talentId").html(talentOptions);
    $("#talentId").prop("disabled", false);
    $("#recruiterId").html(recruiterOptions);
    $("#recruiterId").prop("disabled", false);
  } catch (error) {
    console.error("Error fetching talents and recruiters:", error);

    $("#talentId").html(
      '<option value="" disabled selected>Gagal memuat Talent</option>'
    );
    $("#talentId").prop("disabled", true);
    $("#recruiterId").html(
      '<option value="" disabled selected>Gagal memuat Perekrut/Klien</option>'
    );
    $("#recruiterId").prop("disabled", true);
  }
}

function submit() {
  let position = $("#position").val();
  let recruiterId = $("#recruiterId").val();
  let talentId = $("#talentId").val();
  let offline = $("#selectIsOffline").val();
  let locationAddress = $("#locationAddress").val();
  let interviewLink = $("#interviewLink").val();
  let date = $("#date").val();
  let startTime = $("#startTime").val();
  let endTime = $("#endTime").val();
  let message = $("#message").val();

  $(".is-invalid").removeClass("is-invalid");

  if (
    !position ||
    !recruiterId ||
    !talentId ||
    !offline ||
    !date ||
    !startTime ||
    !endTime
  ) {
    showErrorAlert("Semua kolom yang berlabel required harus diisi");
    if (!position) $("#position").addClass("is-invalid");
    if (!recruiterId) $("#recruiterId").addClass("is-invalid");
    if (!talentId) $("#talentId").addClass("is-invalid");
    if (!offline) $("#selectIsOffline").addClass("is-invalid");
    if (offline === "OFFLINE" && !locationAddress)
      $("#locationAddress").addClass("is-invalid");
    if (offline === "ONLINE" && !interviewLink)
      $("#interviewLink").addClass("is-invalid");
    if (!date) $("#date").addClass("is-invalid");
    if (!startTime) $("#startTime").addClass("is-invalid");
    if (!endTime) $("#endTime").addClass("is-invalid");
    return;
  }

  let status = "ON_PROCESS";

  let data = {
    date: date,
    startTime: startTime,
    endTime: endTime,
    position: position,
    clientId: recruiterId,
    applicantId: talentId,
    interviewType: offline,
    locationAddress: offline === "ONLINE" ? locationAddress : null,
    interviewLink: offline === "OFFLINE" ? interviewLink : null,
    message: message,
    status: status,
  };

  Swal.fire({
    title: "Apakah anda ingin membuat jadwal wawancara ini?",
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
    url: `/admin/interview-schedule/create`,
    method: "POST",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(data),
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Jadwal Wawancara baru berhasil dibuat",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/interview-schedule";
      });
    },

    error: (err) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Jadwal Wawancara baru gagal dibuat",
        showConfirmButton: true,
      });
    },
    complete: () => {
      $.LoadingOverlay("hide");
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
      window.location.href = "/admin/interview-schedule";
    }
  });
  return false;
}
