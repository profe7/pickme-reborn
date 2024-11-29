let intervalDownload;

$(window).on("load", function () {
  if ($("#preloader").length) {
    $("#preloader")
      .delay(100)
      .fadeOut("slow", function () {
        $(this).remove();
      });
    // setTimeout(
    //     function () {
    //         $('#preloader').delay(100).fadeOut('slow', function () {
    //             $(this).remove();
    //         });
    //     }, 1000);
  }
});

function downloadCv(format) {
  document.cookie =
    "isDownloadFinished=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  // showLoadingToast("Sedang mendownload CV...")
  $("#btnDownloadCv").prop("disabled", false);
  if (format === "pdf") {
    $("#btnDownloadCvPdf")
      .prop("disabled", true)
      .html('<i class="fas fa-spinner fa-pulse"></i> Loading');
  } else {
    $("#btnDownloadCvWord")
      .prop("disabled", true)
      .html('<i class="fas fa-spinner fa-pulse"></i> Loading');
  }

  intervalDownload = window.setInterval(function () {
    checkDownload();
  }, 1000);
}

function checkDownload() {
  let isDownloadFinished = false;
  try {
    isDownloadFinished = document.cookie
      .split("; ")
      .find((row) => row.startsWith("isDownloadFinished="))
      .split("=")[1];
    if (isDownloadFinished === "true") {
      clearInterval(intervalDownload);
      setTimeout(function () {});
      document.cookie =
        "isDownloadFinished=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
      iziToast.destroy();
      $("#btnDownloadCv")
        .prop("disabled", false)
        .html('<i class="fa fa-file-pdf"></i> Download CV');
      // $('#btnDownloadCvPdf')
      //     .prop('disabled', true)
      // $('#btnDownloadCvWord')
      //     .prop('disabled', true)
      // // Tutup modal setelah download selesai
      $("#modalDownload").modal("hide");
      resetDownloadButtons(); // Panggil fungsi ini saat download selesai
    }
  } catch (e) {
    console.log(e);
  }
}

function resetDownloadButtons() {
  $("#btnDownloadCvPdf")
    .prop("disabled", false)
    .html('<i class="fa fa-file-pdf"></i> PDF'); // Kembalikan teks tombol ke semula
  $("#btnDownloadCvWord")
    .prop("disabled", false)
    .html('<i class="fa fa-file-word"></i> Word'); // Kembalikan teks tombol ke semula
}

function showLoadingToast(message) {
  iziToast.show({
    timeout: false,
    progressBar: false,
    close: false,
    closeOnEscape: false,
    closeOnClick: false,
    overlay: true,
    displayMode: "once",
    zindex: 999,
    message: message,
    position: "topCenter",
    class: "izitoast_loader",
    image: /img/spinner.gif,
    imageWidth: 45,
  });
}

function convertDate(date) {
  let monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  let d = new Date(date);
  return monthNames[d.getMonth()] + " " + d.getFullYear();
}

function dateFormatBirthDate(dateParam) {
  return dateParam.toString().split("-").reverse().join("-");
}

if ("serviceWorker" in navigator) {
  navigator.serviceWorker
    .register("/js/share-admin/service-worker.js")
    .then(function (registration) {
      console.log("Registration successful, scope is:", registration.scope);
    })
    .catch(function (error) {
      console.log("Service worker registration failed, error:", error);
    });
}

const downloadUsers = () => {
  const data = {
    university: filterUniversity == null ? null : filterUniversity.join("|"),
    major: filterMajor == null ? null : filterMajor.join("|"),
    degree: filterDegree == null ? null : filterDegree.join("|"),
    skill: filterSkill == null ? null : filterSkill.join("|"),
    job: filterWork == null ? null : filterWork.join("|"),
    name: keyword == null || keyword == "" ? null : keyword,
  };

  exportData("/excel-download", JSON.stringify(data))
    .then((data, status, xhr) => {
      const d = new Date();
      var a = document.createElement("a");
      var url = window.URL.createObjectURL(data);
      a.href = url;
      a.download =
        "Users_" +
        d.getFullYear() +
        "-" +
        d.getMonth() +
        "-" +
        d.getDate() +
        "__" +
        d.getHours() +
        "-" +
        d.getMinutes() +
        "-" +
        d.getSeconds() +
        ".xlsx";
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove();
      $("#downloadExcel").prop("disabled", false).html("Download Excel");
      iziToast.destroy();
    })
    .catch((err) => {
      console.log(err);
      console.log("error");
    });

  document.cookie =
    "isDownloadFinished=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  showLoadingToast("Sedang mendownload Data...");
  $("#downloadExcel")
    .prop("disabled", true)
    .html('<i class="fas fa-spinner fa-pulse"></i> Loading');
};
