$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var type1 = $("#param1").val();
  var type2 = $("#param2").val();
  var value = $("#value-param").val();

  // Validasi sederhana untuk kolom yang wajib diisi
  if (!type1 || !type2 || !value) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!type1) $("#param1").addClass("is-invalid");
    if (!type2) $("#param2").addClass("is-invalid");
    if (!value) $("#value-param").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    type1: type1,
    type2: type2,
    value: value,
  });

  Swal.fire({
    title: "Apakah anda ingin membuat parameter ini?",
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
    url: `/api/parameter`,
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
        title: "Parameter berhasil dibuat",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/admin/parameter";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Parameter gagal dibuat",
        showConfirmButton: true,
      });
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
      window.location.href = "/admin/parameter";
    }
  });
  return false;
}
