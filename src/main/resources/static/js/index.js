document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("calendar");

  const dataSrc = () => {
    return new Promise((resolve, reject) => {
      $.ajax({
        type: "GET",
        url: `/api/interview-schedule/recruiter-calendar`,
        dataType: "json",
        success: function (response) {
          console.log(`/api/interview-schedule/recruiter-calendar`);
          console.log(resolve(response))
          resolve(response);
        },
        error: function (error) {
          reject(error);
        },
      });
    });
  };

  const fetchData = async () => {
    try {
      const response = await dataSrc();
      console.log("respon fetchData");
      console.log(response);

      const customButton = document.createElement("button");
      customButton.innerText = "Custom Button";
      customButton.addEventListener("click", function () {
        // Tindakan yang akan dilakukan saat tombol custom diklik
        alert("Tombol Custom Diklik!");
      });

      const calendar = new FullCalendar.Calendar(calendarEl, {
        header: {
          center: "customButton",
          right: "today, prev,next",
        },
        plugins: ["dayGrid", "interaction"],
        allDay: false,
        editable: true,
        selectable: true,
        unselectAuto: false,
        displayEventTime: false,
        events: response,
        eventRender: function (info) {
          info.el.addEventListener("contextmenu", function (e) {
            e.preventDefault();
            let existingMenu = document.querySelector(".context-menu");
            existingMenu && existingMenu.remove();

            const eventIndex = response.findIndex(
              (event) => event.id === info.event.id
            );

            console.log(eventIndex);

            document.body.appendChild(menu);
            menu.style.top = e.pageY + "px";
            menu.style.left = e.pageX + "px";
          });
        },

        eventClick: function (info) {
          loadEventDetails(info.event.id);
        },
      });

      calendar.on("select", function (info) {
        console.log(info);
      });

      calendar.render();
    } catch (error) {
      console.error("Terjadi kesalahan dalam permintaan AJAX:", error);
    }
  };

  fetchData();
});

let selectedEventId;

function loadEventDetails(eventId) {
  selectedEventId = eventId;
  $.ajax({
    url: `/api/interview-schedule/${eventId}/detail`,
    method: "GET",
    dataType: "JSON",
    success: (result) => {
      console.log("loadEventDetails");
      console.log(result);
      // Isi konten modal dengan data yang diterima
      const modalTitle = $("#modal-title");
      const nameElement = $("#name");
      const positionElement = $("#position");
      const dateElement = $("#date");
      const startTimeElement = $("#startTime");
      const endTimeElement = $("#endTime");
      const offlineElement = $("#offline");
      const locationElement = $("#location");
      const linkElement = $("#link");
      const statusElement = $("#status");
      const processButton = $("#processButton");
      const rescheduleButton = $("#rescheduleButton");
      const cancelButton = $("#cancelButton");
      const acceptButton = $("#acceptButton");
      const rejectButton = $("#rejectButton");
      const historyButton = $("#historyButton");

      $("#cancelButton").attr("onclick", `cancel(${result.id})`);
      $("#processButton").attr("onclick", `process(${result.id})`);
      $("#acceptButton").attr("onclick", `accept(${result.id})`);
      $("#rejectButton").attr("onclick", `reject(${result.id})`);

      modalTitle.text("Detail Schedule");
      nameElement.text(result.talent.name);
      positionElement.text(result.position);
      dateElement.text(result.date);
      startTimeElement.text(result.startTime);
      endTimeElement.text(result.endTime);
      offlineElement.text(result.offline ? "Yes" : "No");
      locationElement.html(
        `<span class="fw-bold"> Lokasi : </span> <span>${result.locationAddress}</span>`
      );
      linkElement.html(
        `<span class="fw-bold"> Link : </span> <span>${result.interviewLink}</span>`
      );
      statusElement.text(result.status.name);

      // Tampilkan atau sembunyikan tombol "Reschedule" berdasarkan kondisi
      if (result.offline) {
        $("#location").hide();
        $("#link").show();
      } else {
        $("#location").show();
        $("#link").hide();
      }

      // Process
      if (result.onprocess === false && result.editable === true) {
        processButton.show();
      } else {
        processButton.hide();
      }

      // Reschedule
      if (result.onprocess === false && result.editable === true) {
        rescheduleButton.attr("data-id", result.id);
        rescheduleButton.show();
      } else {
        rescheduleButton.hide();
      }

      // Cancel
      if (result.onprocess === false && result.editable === true) {
        cancelButton.show();
      } else {
        cancelButton.hide();
      }

      // Reject
      if (result.onprocess === true) {
        rejectButton.show();
      } else {
        rejectButton.hide();
      }

      // Accept
      if (result.onprocess === true) {
        acceptButton.show();
      } else {
        acceptButton.hide();
      }

      // History
      historyButton.attr("data-id", result.id);

      // Tampilkan modal
      $("#detailSchedule").modal("show");
    },
  });
}

// Reschedule
$(document).on("click", "#rescheduleButton", function (e) {
  e.preventDefault();
  const eventId = $(this).attr("data-id");
  if (eventId) {
    window.location.href = `/interview-schedule/update/${eventId}`;
  }
});

// History
$(document).on("click", "#historyButton", function (e) {
  e.preventDefault();
  const eventId = $(this).attr("data-id");
  if (eventId) {
    window.location.href = `/interview-schedule/history/${eventId}`;
  }
});

function loading(condition) {
  if (condition) {
    $.LoadingOverlay("show");
  } else {
    $.LoadingOverlay("hide");
  }
}

function cancel(id) {
  Swal.fire({
    title: "Apakah anda ingin membatalkan wawancara ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var url = `/api/interview-schedule/${id}/cancel`;
      $("#detailSchedule").modal("hide");
      feedback(id, url);
    }
  });
}

function process(id) {
  Swal.fire({
    title: "Apakah anda ingin memproses wawancara ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var data = JSON.stringify({});
      var url = `/api/interview-schedule/${id}/process`;
      $("#detailSchedule").modal("hide");
      save(id, data, url);
    }
  });
}

function accept(id) {
  Swal.fire({
    title: "Apakah anda ingin menerima talent ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var url = `/api/interview-schedule/${id}/accept`;
      $("#detailSchedule").modal("hide");
      feedBackWithOnBoardDate(id, url);
    }
  });
}

function reject(id) {
  Swal.fire({
    title: "Apakah anda ingin menolak talent ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var url = `/api/interview-schedule/${id}/reject`;
      $("#detailSchedule").modal("hide");
      feedback(id, url);
    }
  });
}

function feedBackWithOnBoardDate(id, url) {
  Swal.fire({
    title: "Notes",
    html:
      '<label for="feedback">Saran</label>' +
      '<input type="text" id="feedback" class="swal2-input" placeholder="Masukkan saran">' +
      '<label for="onboard">Tanggal mulai</label>' +
      '<input type="date" id="onboard" class="swal2-input" placeholder="Masukkan tanggal mulai bekerja"  min="' +
      getCurrentDate() +
      '">',
    showCancelButton: true,
    confirmButtonText: "Submit",
    cancelButtonText: "Batal",
    focusConfirm: false,
    preConfirm: () => {
      // Retrieve values from the inputs
      const feedback = document.getElementById("feedback").value;
      const onboard = document.getElementById("onboard").value;

      // Validate inputs
      if (!feedback || !onboard) {
        Swal.showValidationMessage("Tolong isi semuanya");
      } else {
        // You can do something with the valid input values here
        loading(true);
        var data = JSON.stringify({
          feedback: feedback,
          onBoardDate: onboard,
        });
        save(id, data, url);
      }
    },
    allowOutsideClick: () => !Swal.isLoading(),
  });
}

function getCurrentDate() {
  const today = new Date();
  const year = today.getFullYear();
  let month = today.getMonth() + 1;
  let day = today.getDate();

  // Add leading zero to single-digit months and days
  month = month < 10 ? "0" + month : month;
  day = day < 10 ? "0" + day : day;

  return `${year}-${month}-${day}`;
}

function feedback(id, url) {
  Swal.fire({
    title: "Notes",
    input: "text",
    inputAttributes: {
      autocapitalize: "off",
    },
    showCancelButton: true,
    confirmButtonText: "Kirim",
    cancelButtonText: `Batal`,
    showLoaderOnConfirm: true,
    inputValidator: (value) => {
      if (!value) {
        return "Anda harus menuliskan saran!";
      }
    },
    preConfirm: (input) => {
      loading(true);
      var data = JSON.stringify({
        feedback: input,
      });
      save(id, data, url);
    },
    allowOutsideClick: () => !Swal.isLoading(),
  }).then((result) => {
    console.log("successs");
  });
}

function save(id, data, url) {
  $.ajax({
    url: url,
    method: "PUT",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      // Menyembunyikan overlay loading
      console.log("save action");
      console.log(result);
      loading(false);

      Swal.fire({
        position: "center",
        icon: "success",
        title: "Data berhasil diperbarui",
        showConfirmButton: false,
        timer: 1500,
      }).then(() => {
        // Memuat ulang halaman
        location.reload();
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      loading(false);

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Data gagal diperbarui",
        showConfirmButton: false,
        timer: 1500,
      });
    },
  });
}

// function invite(talentId) {
//   var data = JSON.stringify({ id: talentId });
//   $.ajax({
//     type: "POST",
//     url: `/api/talent/encrypt`,
//     data: data,
//     dataType: "text",
//     beforeSend: addCsrfToken(),
//     contentType: "application/json",
//     success: function (response) {
//       window.location.href = `/interview-schedule/create?token=${response}`;
//     },
//   });
// }

function invite(talentId, recommendationId) {
  var data = JSON.stringify({ id: talentId });
  $.ajax({
    type: "POST",
    url: `/api/talent/encrypt`,
    data: data,
    dataType: "text",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    success: function (response) {
      console.log(recommendationId);
      window.location.href = `/interview-schedule/create?token=${response}&recommendationId=${recommendationId}`;
    },
  });
}

function cv(id) {
  $("#container").LoadingOverlay("show");
  $.ajax({
    url: `/api/talent/${id}`,
    method: "GET",
    success: (result) => {
      window.open(
        `https://dev.cv-me.metrodataacademy.id/share/${result.userUrl}`
      );
      $("#container").LoadingOverlay("hide", true);
    },
    error: (e) => {
      $("#container").LoadingOverlay("hide", true);
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Gagal mencari CV",
        showConfirmButton: false,
        timer: 1500,
      });
    },
  });
}
