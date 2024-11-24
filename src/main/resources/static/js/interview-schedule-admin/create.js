$(document).ready(function () {
  $(".select2").select2();

  // Event listener untuk perubahan nilai pada select box #selectIsOffline
  $("#selectIsOffline").change(function () {
    var selectedValue = $(this).val();

    if (selectedValue === "true") {
      $("#locationAddress").prop("disabled", false).val("");
      $("#interviewLink").prop("disabled", true).val("");
    } else if (selectedValue === "false") {
      $("#interviewLink").prop("disabled", false).val("");
      $("#locationAddress").prop("disabled", true).val("");
    }
  });

  $("#ccSelect").select2({
    tags: true,
    tokenSeparators: [",", " "],
  });
});

function submit() {
  let ccSelect = $("#ccSelect").val() || [];
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

  // Clear previous validation messages
  $(".is-invalid").removeClass("is-invalid");

  // Validasi field yang dibutuhkan
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
    // Tambahkan kelas "is-invalid" ke field yang tidak terisi
    if (!ccSelect.length) $("#ccSelect").addClass("is-invalid");
    if (!position) $("#position").addClass("is-invalid");
    if (!recruiterId) $("#recruiterId").addClass("is-invalid");
    if (!talentId) $("#talentId").addClass("is-invalid");
    if (!offline) $("#selectIsOffline").addClass("is-invalid");
    if (offline === "true" && !locationAddress)
      $("#locationAddress").addClass("is-invalid");
    if (offline === "false" && !interviewLink)
      $("#interviewLink").addClass("is-invalid");
    if (!date) $("#date").addClass("is-invalid");
    if (!startTime) $("#startTime").addClass("is-invalid");
    if (!endTime) $("#endTime").addClass("is-invalid");
    return;
  }

  let statusId = 3;

  let data = {
    ccSelect: ccSelect,
    date: date,
    startTime: startTime,
    endTime: endTime,
    position: position,
    recruiterId: recruiterId,
    talentId: talentId.length > 0 ? talentId[0] : null,
    offline: offline === "true",
    locationAddress: offline === "true" ? locationAddress : null,
    interviewLink: offline === "false" ? interviewLink : null,
    message: message,
    statusId: statusId,
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

  console.log("Data yang akan dikirim:", data);
}

function create(data) {
  $.ajax({
    url: `/admin/interview-schedule/create`,
    method: "POST",
    dataType: "json",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: JSON.stringify(data),
    success: (result) => {
      console.log("Success result:", result);
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Jadwal Wawancara berhasil dibuat",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/interview-schedule";
      });
    },

    error: (err) => {
      console.log("Error:", err);
      console.log("Error status:", err.status);
      console.log("Error status text:", err.statusText);
      console.log("Error response text:", err.responseText);
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
    title: "Apakah anda yakin ingin kembali?",
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
