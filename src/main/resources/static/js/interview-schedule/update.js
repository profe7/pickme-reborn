$(document).ready(function () {
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

function loading(condition) {
  if (condition) {
    $.LoadingOverlay("show");
  } else {
    $.LoadingOverlay("hide");
  }
}
let validationFailed = (message) => {
  Swal.fire({
    position: "center",
    icon: "error",
    title: `${message}`,
    showConfirmButton: true,
  });
};
$(document).ready(function () {
  var offline = $("#offline").val();
  if (offline == "false") {
    $("#locationAddressLabel").show();
    $("#locationAddress").show();
    $("#interviewLinkLabel").hide();
    $("#interviewLink").hide();
    $("#interviewLink").val("");
  } else {
    $("#locationAddressLabel").hide();
    $("#locationAddress").hide();
    $("#locationAddress").val("");
    $("#interviewLinkLabel").show();
    $("#interviewLink").show();
  }
});
function interviewTypeHandler(data) {
  if (data == "false") {
    $("#locationAddressLabel").show();
    $("#locationAddress").show();
    $("#interviewLinkLabel").hide();
    $("#interviewLink").hide();
    var oldType = $("#oldType").val();
    console.log(oldType);
    var message = $("#message").val();
    message = message.replace(oldType, "Offline");
    $("#message").val(message);
    $("#oldType").val("Offline");
  } else {
    $("#locationAddressLabel").hide();
    $("#locationAddress").hide();
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
  var oldDetail = $("#oldDetail").val();
  var message = $("#message").val();
  message = message.replace(oldDetail, value);
  console.log(value);
  console.log(oldDetail);
  $("#message").val(message);
  $("#oldDetail").val(value);
}

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
        validationFailed("Can't do interview on holiday");
        $("#date").val("");
      } else {
        var oldDate = $("#oldDate").val();
        var message = $("#message").val();
        message = message.replace(oldDate, date);
        $("#message").val(message);
        $("#oldDate").val(date);
      }
      loading(false);
    },
    error: (e) => {
      loading(false);
      validationFailed("Terjadi kesalahan");
    },
  });
}

function timeHandler() {
  var interviewScheduleId = $("#interviewId").val();
  var talentId = $("#talentId").val();
  var date = $("#date").val();
  var startTime = $("#startTime").val();
  var endTime = $("#endTime").val();

  var oldStartTime = $("#oriStart").val();
  var oldEndTime = $("#oriEnd").val();

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
    // Check if the start time is later than the end time
    if (compareTimes(startTime, endTime) > 0) {
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Waktu mulai tidak boleh melebihi waktu selesai",
      }).then(() => {
        $("#startTime").val(oldStartTime);
        $("#endTime").val(oldEndTime);
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
        $("#startTime").val(oldStartTime);
        $("#endTime").val(oldEndTime);
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
    talentHandler(talentId, date, startTime, endTime, interviewScheduleId);
  }
}
function talentHandler(
  talentId,
  date,
  startTime,
  endTime,
  interviewScheduleId
) {
  var data = JSON.stringify({
    talentId,
    interviewScheduleId,
    date,
    startTime,
    endTime,
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
        validationFailed("Talent sudah memiliki jadwal interview saat ini");
        $("#date").val("");
        $("#startTime").val("");
        $("#endTime").val("");
      } else {
        var oldDate = $("#oldDate").val();
        var oldStart = $("#oldStart").val();
        var oldEnd = $("#oldEnd").val();
        console.log(oldDate);
        console.log(oldStart);
        var message = $("#message").val();
        message = message.replace(oldDate, date);
        message = message.replace(oldStart, startTime);
        message = message.replace(oldEnd, endTime);
        $("#message").val(message);
        $("#oldDate").val(date);
        $("#oldStart").val(startTime);
        $("#oldEnd").val(endTime);
      }
      loading(false);
    },
    error: (e) => {
      loading(false);
      validationFailed("Terjadi kesalahan");
    },
  });
}

function submit() {
  var interviewScheduleId = $("#interviewId").val();
  var talentId = $("#talentId").val();
  var date = $("#date").val();
  var startTime = $("#startTime").val();
  var endTime = $("#endTime").val();
  var position = $("#position").val();
  var offline = $("#offline").val();
  var interviewLink = $("#interviewLink").val();
  var locationAddress = $("#locationAddress").val();
  var message = $("#message").val();
  var feedback = $("#feedback").val();
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
    feedback: feedback,
    cc: selectedCc,
    jobId: jobId,
  });

  Swal.fire({
    title: "Apakah anda ingin menyimpan perubahan ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    if (result.isConfirmed) {
      loading(true);
      update(interviewScheduleId, data);
    }
  });
}
function update(id, data) {
  $.ajax({
    url: `/api/interview-schedule/${id}/update`,
    method: "PUT",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      loading(false);

      Swal.fire({
        position: "center",
        icon: "success",
        title: "Data berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/interview-schedule/";
      });
    },
    error: (e) => {
      loading(false);
      validationFailed("Terjadi kesalahan");
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

// Function to filter talents by name
function filterTalentsName() {
  let talentName = document.getElementById('searchTalentName').value;
  let clientId = document.getElementById('clientId').value;

  fetchRecommendations(clientId, talentName);
}

// Function to fetch recommendations from the server
function fetchRecommendations(clientId, talentName) {
  $.ajax({
    url: `/client/interview-schedules`,
    type: 'GET',
    data: {
      clientId: clientId,
      search: talentName
    },
    success: function(data) {
      $('#talent-interview-schedules').empty().html($(data).find('#talent-interview-schedules').html());
      $('#pagination-controls').empty().html($(data).find('#pagination-controls').html());
    },
    error: function(error) {
      console.error('Error fetching recommendations:', error);
    }
  });
}
