$(document).ready(function () {
  $(".select2").select2();
});

function submit() {
  var jobId = $("#jobId").val();
  var title = $("#title").val();
  var position = $("#position").val();
  var expiredDate = $("#expiredDate").val();
  var description = $("#description").val();
  var talent = $("#talent").val();

  var data = JSON.stringify({
    jobId: jobId,
    title: title,
    position: position,
    expiredDate: expiredDate,
    description: description,
    talent: talent,
  });

  Swal.fire({
    title: "Apakah anda ingin membuat lamaran ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      create(data);
    }
  });
}

function create(data) {
  $.ajax({
    url: `/api/applicant`,
    method: "POST",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "success",
        title: "Lamaran berhasil dibuat",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/talent/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Lamaran gagal dibuat, ada kesalahan",
        showConfirmButton: true,
      });
    },
  });
}
