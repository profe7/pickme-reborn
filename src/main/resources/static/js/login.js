$(document).ready(function () {});

function submit() {
  var emailOrUsername = $("#emailorusername").val();

  var data = JSON.stringify({
    emailOrUsername: emailOrUsername,
  });

  Swal.fire({
    title: "Kirim?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    denyButtonText: `Tidak`,
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      forget(data);
    }
  });
}

function forget(data) {
  console.log(data);
  $.ajax({
    url: `/api/forget-password`,
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
        title: "Silahkan cek email anda.",
        showConfirmButton: true,
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Email gagal terkirim.",
        showConfirmButton: true,
      });
    },
  });
}
