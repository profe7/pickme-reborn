$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var name = $("#name").val();
  var privilegeIds = [];

  // Validasi sederhana untuk kolom yang wajib diisi
  if (!name) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Kolom nama peran harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!name) $("#name").addClass("is-invalid");

    return;
  }

  // Mengumpulkan nilai checkbox yang dicentang
  $(".privilegeCheckbox:checked").each(function () {
    privilegeIds.push($(this).val());
  });

  var data = JSON.stringify({
    name: name,
    privilegeIds: privilegeIds,
  });

  Swal.fire({
    title: "Apakah anda ingin membuat peran ini?",
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
    url: `/admin/create`,
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
        title: "Peran berhasil dibuat",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/role/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Peran gagal dibuat",
        showConfirmButton: true,
      });
    },
  });
}
