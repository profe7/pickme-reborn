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

  // $("#ccSelect").select2({
  //   tags: true,
  //   tokenSeparators: [",", " "],
  // });
});

function submit() {
  let ccSelect = $("#ccSelect").val();
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
    !ccSelect.length ||
    !position ||
    !recruiterId ||
    !talentId ||
    !offline ||
    !date ||
    !startTime ||
    !endTime
  ) {
    showErrorAlert("Semua kolom yang berlabel required harus diisi");
    if (!ccSelect.length) $("#ccSelect").addClass("is-invalid");
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
    ccSelect: ccSelect,
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
        icon: "success",
        title: "Jadwal Wawancara berhasil dibuat",
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
