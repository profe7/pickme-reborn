$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var holidayId = $("#holidayId").val();
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
    title: "Apakah anda ingin menyimpan perubahan ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(holidayId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/admin/holiday/update/${id}`,
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
        title: "Hari libur berhasil diperbarui",
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
        title: "Hari libur gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}
