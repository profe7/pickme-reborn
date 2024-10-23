// $(document).ready(function () {
//   $("#message").summernote({
//     height: 150,
//     minHeight: null,
//     maxHeight: null,
//   });
// });


$(document).ready(function () {
  $("#locationAddressLabel").hide();
  $("#locationAddress").hide();
  $(".select2").select2();
  $("#ccSelect").select2({
    tags: true,
    tokenSeparators: [",", " "],
  });

  // Add validation for email format
  $("#ccSelect").on("select2:selecting", function (e) {
    var input = e.params.args.data.text;
    var isValidEmail = validateEmail(input);

    if (!isValidEmail) {
      e.preventDefault(); // Prevent adding invalid email
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Email tidak valid. Harap masukkan email yang valid.",
      });
    }
  });

  // Fungsi untuk validasi email
  function validateEmail(email) {
    // Gunakan regex atau logika validasi email sesuai kebutuhan
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
});

function isLinkValid() {
  var inputValue = $("#interviewLink").val();
  var isValidLink = /(https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z]{2,}(\.[a-zA-Z]{2,})(\.[a-zA-Z]{2,})?\/[a-zA-Z0-9]{2,}|((https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z]{2,}(\.[a-zA-Z]{2,})(\.[a-zA-Z]{2,})?)|(https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z0-9]{2,}\.[a-zA-Z0-9]{2,}\.[a-zA-Z0-9]{2,}(\.[a-zA-Z0-9]{2,})?/g;
  

  return isValidLink.test(inputValue);
}

function showErrorMessage(message) {
  Swal.fire({
      icon: "error",
      title: "Error",
      text: message,
  });
}

function isLinkValid() {
  var inputValue = $("#interviewLink").val();
  var isValidLink = /(https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z]{2,}(\.[a-zA-Z]{2,})(\.[a-zA-Z]{2,})?\/[a-zA-Z0-9]{2,}|((https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z]{2,}(\.[a-zA-Z]{2,})(\.[a-zA-Z]{2,})?)|(https:\/\/www\.|http:\/\/www\.|https:\/\/|http:\/\/)?[a-zA-Z0-9]{2,}\.[a-zA-Z0-9]{2,}\.[a-zA-Z0-9]{2,}(\.[a-zA-Z0-9]{2,})?/g;
  

  return isValidLink.test(inputValue);
}

function showErrorMessage(message) {
  Swal.fire({
      icon: "error",
      title: "Error",
      text: message,
  });
}

function interviewTypeHandler(data) {
  if (data == "false") {
    $("#locationAddressLabel").show();
    $("#locationAddress").show();
    $("#interviewLinkLabel").hide();
    $("#interviewLink").hide();
    $("#interviewLink").val("");
    var oldType = $("#oldType").val();
    console.log(oldType);
    var message = $("#message").val();
    message = message.replace(oldType, "Offline");
    $("#message").val(message);
    $("#oldType").val("Offline");
  } else {
    $("#locationAddressLabel").hide();
    $("#locationAddress").hide();
    $("#locationAddress").val("");
    $("#interviewLinkLabel").show();
    $("#interviewLink").show();
    var oldType = $("#oldType").val();
    console.log(oldType);
    var message = $("#message").val();
    message = message.replace(oldType, "Online");
    $("#message").val(message);
    $("#oldType").val("Online");
  }
}

function detailHandler(value) {
  const oldDetailValue = $("#oldDetail").val();
  let messageValue = $("#message").val();

  messageValue =
    oldDetailValue !== ""
      ? messageValue.replace(oldDetailValue, value)
      : messageValue.trim() + " " + value;

  $("#message").val(messageValue);
  $("#oldDetail").val(value);
}

// $.get('/api/recommendation', function (data) {
//   var position = data.position;
//   // Panggil fungsi positionHandler dengan nilai posisi rekomendasi
//   positionHandler(position);
// });

function positionHandler(position) {
  var oldPosition = $("#oldPosition").val();
  var message = $("#message").val();
  message = message.replace(oldPosition, position);
  console.log(position);
  console.log(oldPosition);
  $("#message").val(message);
  $("#oldPosition").val(position);
}



function dateHandler(date) {
  var data = JSON.stringify({
    date: date,
  });
  $.ajax({
    url: `/api/inverview-schedule-check/date`,
    method: "POST",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      if (result == false) {
        Swal.fire({
          position: "center",
          icon: "error",
          title:
            "Tidak bisa membuat jadwal wawancara pada hari libur dan akhir pekan",
          showConfirmButton: false,
          timer: 1500,
        });
        $("#date").val("");
      } else {
        var oldDate = $("#oldDate").val();
        var message = $("#message").val();
        message = message.replace(oldDate, date);
        $("#message").val(message);
        $("#oldDate").val(date);
      }
      $.LoadingOverlay("hide");
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Terjadi kesalahan. Mohon coba lagi.",
        showConfirmButton: false,
        timer: 1500,
      });
    },
  });
}

function timeHandler() {
  var talentId = $("#talentId").val();
  var date = $("#date").val();
  var startTime = $("#startTime").val();
  var endTime = $("#endTime").val();

  if (date !== "" && date !== null && startTime !== "" && startTime !== null) {
    var today = new Date();
    var selectedDate = new Date(date);

    if (selectedDate.toDateString() === today.toDateString()) {
      var currentTime = today.getTime();
      var selectedTime = new Date(
        today.toDateString() + " " + startTime
      ).getTime();
      var oneHourFromNow = currentTime + 60 * 60 * 1000;

      if (selectedTime < oneHourFromNow) {
        Swal.fire({
          icon: "error",
          title: "Error",
          text: "Waktu mulai harus minimal 1 jam dari waktu saat ini",
        }).then(() => {
          $("#startTime").val("");
        });
        return;
      }
    }
  }

  if (startTime && endTime) {
    if (compareTimes(startTime, endTime) > 0) {
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Waktu mulai tidak boleh melebihi waktu selesai",
      }).then(() => {
        $("#startTime").val("");
        $("#endTime").val("");
      });
      return;
    }

    // Check if startTime is equal to endTime
    if (compareTimes(startTime, endTime) === 0) {
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Waktu mulai tidak boleh sama dengan waktu selesai",
      }).then(() => {
        $("#startTime").val("");
        $("#endTime").val("");
      });
      return;
    }
  }

  if (
    date !== "" &&
    date !== null &&
    startTime !== "" &&
    startTime !== null &&
    endTime !== "" &&
    endTime !== null
  ) {
    talentHandler(talentId, date, startTime, endTime);
  }
}
function talentHandler(talentId, date, startTime, endTime) {
  var data = JSON.stringify({
    talentId,
    date,
    startTime,
    endTime,
    interviewScheduleId: null,
  });
  $.ajax({
    url: `/api/inverview-schedule-check/talent`,
    method: "POST",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      if (result == false) {
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Talent tidak  tersedia pada jadwal yang telah dipilih",
          showConfirmButton: false,
          timer: 1500,
        });
        $("#date").val("");
        $("#startTime").val("");
        $("#endTime").val("");
      } else {
        var oldDate = $("#oldDate").val();
        var oldStart = $("#oldStart").val();
        var oldEnd = $("#oldEnd").val();
        var message = $("#message").val();
        message = message.replace(oldDate, date);
        message = message.replace(oldStart, startTime);
        message = message.replace(oldEnd, endTime);
        $("#message").val(message);
        $("#oldDate").val(date);
        $("#oldStart").val(startTime);
        $("#oldEnd").val(endTime);
      }
      $.LoadingOverlay("hide");
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Terjadi kesalahan. Mohon coba lagi.",
        showConfirmButton: false,
        timer: 1500,
      });
    },
  });
}

function submit() {
  var talentId = $("#talentId").val();
  var date = $("#date").val();
  var startTime = $("#startTime").val();
  var endTime = $("#endTime").val();
  var position = $("#position").val();
  var offline = $("#offline").val();
  var interviewLink = $("#interviewLink").val();
  var locationAddress = $("#locationAddress").val();
  var message = $("#message").val();
  var selectedCc = $("#ccSelect").val();
  var jobId = $("#jobId").val();

  // Simple required field validation
  if (!date || !startTime || !endTime || !position || !message) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menghapus pesan invalid sebelum validasi
    $(".is-invalid").removeClass("is-invalid");

    if (!date) $("#date").addClass("is-invalid");
    if (!startTime) $("#startTime").addClass("is-invalid");
    if (!endTime) $("#endTime").addClass("is-invalid");
    if (!position) $("#position").addClass("is-invalid");
    if (!message) $("#message").addClass("is-invalid");
    if (offline === "true" && !interviewLink)
      $("#interviewLink").addClass("is-invalid");
    if (offline === "false" && !locationAddress)
      $("#interviewLink").addClass("is-invalid");

    return;
  }

  if (offline === "true" && !interviewLink) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Link Wawancara tidak boleh kosong",
    });
    $("#interviewLink").addClass("is-invalid");
    return;
  } else {
    $("#interviewLink").removeClass("is-invalid");
  }

  if (offline === "false" && !locationAddress) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Lokasi tidak boleh kosong",
    });
    $("#locationAddress").addClass("is-invalid");
    return;
  } else {
    $("#locationAddress").removeClass("is-invalid");
  }

  // if (!validateForm()) {
  //       showErrorMessage("Semua kolom dengan label required harus diisi");
  //       return;
  //   }

    // Pemeriksaan tambahan sesuai kebutuhan
    if (offline === "true" && !isLinkValid()) {
      showErrorMessage("Link Wawancara tidak valid. Harap masukkan URL yang valid.");
      return;
    }

    // // Pemeriksaan tambahan sesuai kebutuhan
    // if (!isLinkValid()) {
    //     showErrorMessage("Link Wawancara tidak valid. Harap masukkan URL yang valid.");
    //     // console.log(isLinkValid)
    //     return;
    // }

  var data = JSON.stringify({
    talentId: talentId,
    date: date,
    startTime: startTime,
    endTime: endTime,
    position: position,
    offline: offline,
    interviewLink: interviewLink,
    locationAddress: locationAddress,
    message: message,
    cc: selectedCc,
    jobId: jobId,
  });

  Swal.fire({
    title: "Apakah anda ingin membuat jadwal wawancara ini?",
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
    url: `/api/interview-schedule`,
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
        title: "Jadwal Wawancara berhasil dibuat",
        showConfirmButton: true,
      }).then(() => {
        // Kembali ke halaman interview
        window.location.href = "/interview-schedule/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Jadwal Wawancara gagal dibuat, ada kesalahan",
        showConfirmButton: true,
      });
    },
  });
}

function compareTimes(time1, time2) {
  const [hours1, minutes1] = time1.split(":").map(Number);
  const [hours2, minutes2] = time2.split(":").map(Number);

  if (hours1 > hours2) {
    return 1;
  } else if (hours1 < hours2) {
    return -1;
  } else {
    // Hours are equal, compare minutes
    if (minutes1 > minutes2) {
      return 1;
    } else if (minutes1 < minutes2) {
      return -1;
    } else {
      // Minutes are also equal
      return 0;
    }
  }
}

function buttonBack() {
  var referrer = document.referrer;

  // get element button back
  var backBtn = document.getElementById("btnBack");

  if (referrer.includes('/talent')) {
    backBtn.href = '/talent';
  } else if (referrer.includes('/recommendation')) {
    backBtn.href = '/recommendation';
  } else {
    backBtn.href = '/';
  }
}
