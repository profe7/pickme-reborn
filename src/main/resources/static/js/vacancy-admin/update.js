var editorInstance;

$(document).ready(function () {
  ClassicEditor.create(document.querySelector("#editor"))
    .then((editor) => {
      editorInstance = editor;
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

  $(".is-invalid").removeClass("is-invalid");

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
  var status = $("#job-status").val();
  var requiredPositions = $("#required-positions").val();

  if (requiredPositions <= 0) {
    document.getElementById("invalid-required").innerHTML =
      "Masukan Bilangan Bulat Positif";
    $("#required-positions").addClass("is-invalid");
    return;
  }

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

    if (!title) $("#title").addClass("is-invalid");
    if (!description) $("#description").addClass("is-invalid");
    if (!position) $("#position").addClass("is-invalid");
    if (!expiredDate) $("#expiredDate").addClass("is-invalid");
    if (!status) $("#job-status").addClass("is-invalid");
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
    title: "Apakah anda yakin ingin memperbarui 'Lowongan' ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(jobId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/admin/vacancy/update/${id}`,
    method: "PUT",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "success",
        title: "Lowongan berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/vacancy";
      });
    },
    error: (e) => {
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

function confirmBack() {
  Swal.fire({
    title: "Apakah anda yakin ingin kembali?",
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed) {
      window.location.href = "/admin/vacancy";
    }
  });
  return false;
}
