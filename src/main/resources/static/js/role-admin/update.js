function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var roleId = $("#roleId").val();
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
    title: "Apakah anda ingin menyimpan perubahan ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(roleId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/api/role/${id}`,
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
        title: "Peran berhasil diperbarui",
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
        title: "Peran gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}
