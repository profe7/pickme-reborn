$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var name = $("#name").val();
  var description = $("#description").val();
  var date = $("#date").val();

  // Validasi sederhana untuk kolom yang wajib diisi
  if (!name || !description || !date) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!name) $("#name").addClass("is-invalid");
    if (!description) $("#description").addClass("is-invalid");
    if (!date) $("#date").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    name: name,
    description: description,
    date: date,
  });

  Swal.fire({
    title: "Apakah anda ingin membuat hari libur ini?",
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
    url: `/admin/holiday/create`,
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
        title: "Hari libur berhasil dibuat",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/holiday/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Hari libur gagal dibuat",
        showConfirmButton: true,
      });
    },
  });
}
