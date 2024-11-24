$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var messageTemplateId = $("#messageTemplateId").val();
  var type = $("#type").val();
  var message = $("#message").val();

  // Validasi sederhana untuk kolom yang wajib diisi
  if (!type || !message) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!type) $("#type").addClass("is-invalid");
    if (!message) $("#message").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    type: type,
    message: message,
  });

  Swal.fire({
    title: "Apakah anda ingin menyimpan perubahan ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(messageTemplateId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/api/message-template/${id}`,
    method: "PUT",
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
        title: "Templat pesan berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/message-template/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Templat pesan gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}
