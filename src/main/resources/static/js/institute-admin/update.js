$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var instituteId = $("#instituteId").val();
  var name = $("#name").val();
  var rmId = $("#rmId").val();

  var instituteTypeId = $("#instituteTypeId").val();

  // Validasi sederhana untuk kolom yang wajib diisi
  if (!name || !rmId || !instituteTypeId) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!name) $("#name").addClass("is-invalid");
    if (!rmId) $("#rmId").addClass("is-invalid");
    if (!instituteTypeId) $("#instituteTypeId").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    name: name,
    rmId: rmId,

    instituteTypeId: instituteTypeId,
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
      update(instituteId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/admin/institute/update/${id}`,
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
        title: "Instansi berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/institute/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Instansi gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}
