$(document).ready(function () {
  ClassicEditor.create(document.querySelector("#description"))
    .then((editor) => {
      editorInstance = editor; // Store the CKEditor instance
      // editor.setData(''); // Set initial content (optional)
    })
    .catch((error) => {
      console.error(error);
    });

  $(".select2").select2();
});
function submit() {
  var today = new Date();
  var selectedDate = new Date($("#expiredDate").val());

  if (selectedDate < today) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Tanggal wawancara tidak boleh sebelum hari ini",
    });
    $("#expiredDate").addClass("is-invalid");
    return;
  } else {
    $("#expiredDate").removeClass("is-invalid");
  }

  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  // Check if editor instance is available
  if (!editorInstance) {
    console.error(
      "CKEditor instance not found. Please ensure it is initialized properly."
    );
    return;
  }

  var description = editorInstance.getData();

  var jobId = $("#jobId").val();
  var title = $("#title").val();
  var position = $("#position").val();
  var expiredDate = $("#expiredDate").val();
  var status = $("#status").val();
  var requiredPositions = $("#required-positions").val();

  if (requiredPositions <= 0) {
    document.getElementById("invalid-required").innerHTML =
      "Masukan Bilangan Bulat Positif";
    $("#required-positions").addClass("is-invalid");
    return;
  }

  // Validasi sederhana untuk kolom yang wajib diisi
  if (
    !title ||
    !description ||
    !position ||
    !expiredDate ||
    !status ||
    !requiredPositions
  ) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!title) $("#title").addClass("is-invalid");
    if (!description) $("#description").addClass("is-invalid");
    if (!position) $("#position").addClass("is-invalid");
    if (!expiredDate) $("#expiredDate").addClass("is-invalid");
    if (!status) $("#status").addClass("is-invalid");
    if (!requiredPositions) $("#required-positions").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    title: title,
    position: position,
    description: description,
    expiredDate: expiredDate,
    status: status,
    requiredPositions: parseInt(requiredPositions),
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
      update(jobId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/api/job/${id}`,
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
        title: "Lowongan berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/job";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Lowongan gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}
