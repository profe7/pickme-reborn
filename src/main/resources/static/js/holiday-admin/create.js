$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var name = $("#name").val();
  var description = $("#description").val();
  var date = $("#date").val();
  var fileInput = $("#customFile")[0].files;

  if (fileInput.length > 0) {
    handleFileUpload(fileInput[0]);
    return;
  }

  if (!name || !description || !date) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

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
    title: "Apakah Anda yakin ingin membuat 'Hari Libur' ini?",
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

function handleFileUpload(file) {
  if (!file.name.endsWith(".json")) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Hanya file dengan format .json yang diperbolehkan",
    });
    return;
  }

  var formData = new FormData();
  formData.append("json", file);

  Swal.fire({
    title: "Apakah Anda yakin ingin mengunggah file JSON 'Hari Libur' ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      uploadFile(formData);
    }
  });
}

function uploadFile(formData) {
  $.ajax({
    url: `/admin/holiday/upload`,
    method: "POST",
    processData: false,
    contentType: false,
    data: formData,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "File JSON berhasil diunggah",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/holiday";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Gagal mengunggah file JSON",
        showConfirmButton: true,
      });
    },
  });
}

function create(data) {
  $.ajax({
    url: `/admin/holiday/create`,
    method: "POST",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Hari Libur baru berhasil ditambahkan",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/holiday";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Hari Libur baru gagal ditambahkan",
        showConfirmButton: true,
      });
    },
  });
}

function confirmBack() {
  Swal.fire({
    title: "Apakah Anda yakin ingin kembali?",
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed) {
      window.location.href = "/admin/holiday";
    }
  });
  return false;
}
