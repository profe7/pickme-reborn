let popoverContent = "";

$(window).on("load", function () {
  $("#frame").contents().find(".main-app").css("zoom", "40%");
  $("#frame").css("height", "300px");

  let cvProgress = $("#cv-progress");
  let progress = getProgress();

  cvProgress.css("width", progress + "%");
  cvProgress.attr("aria-valuenow", progress);
  cvProgress.text(progress + "%");

  $("#popover").attr("data-content", popoverContent);
});

const getProgress = () => {
  let totalProgress = 0;

  let personalData = JSON.parse(localStorage.getItem("personalData"));
  let awardData = JSON.parse(localStorage.getItem("awardData"));
  let trainingData = JSON.parse(localStorage.getItem("trainingData"));
  let certificationData = JSON.parse(localStorage.getItem("certificationData"));
  let skillData = JSON.parse(localStorage.getItem("skillData"));
  let educationData = JSON.parse(localStorage.getItem("educationData"));
  let workData = JSON.parse(localStorage.getItem("workData"));
  let organizationData = JSON.parse(localStorage.getItem("organizationData"));
  let experienceData = JSON.parse(localStorage.getItem("experienceData"));
  let languageData = JSON.parse(localStorage.getItem("languageData"));
  let projectData = JSON.parse(localStorage.getItem("projectData"));

  let emptyPersonalData = 0;
  for (const key in personalData) {
    if (personalData[key] == "") {
      emptyPersonalData += 1;
    }
  }

  if (emptyPersonalData == 0) {
    totalProgress += personalPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Pribadi Belum Lengkap<br>`;
  }

  if (awardData.length > 0) {
    totalProgress += awardPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Penghargaan Belum Diisi<br>`;
  }

  if (projectData.length > 0) {
    totalProgress += projectPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Project Belum Diisi<br>`;
  }

  if (trainingData.length > 0) {
    totalProgress += trainingPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Pelatihan Belum Diisi<br>`;
  }

  if (certificationData.length > 0) {
    totalProgress += certificationPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Sertifikasi Belum Diisi<br>`;
  }

  if (skillData.length > 0) {
    totalProgress += skillPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Skill Belum Diisi<br>`;
  }

  if (educationData.length > 0) {
    totalProgress += educationPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Pendidikan Belum Diisi<br>`;
  }

  if (workData.length > 0) {
    totalProgress += workPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Riwayat Pekerjaan Belum Diisi<br>`;
  }

  if (organizationData.length > 0) {
    totalProgress += organizationPercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Organisasi Belum Diisi<br>`;
  }

  if (experienceData.length > 0) {
    totalProgress += experiencePercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Pengalaman Belum Diisi<br>`;
  }

  if (languageData.length > 0) {
    totalProgress += languagePercentage;
  } else {
    popoverContent += `<span class='text-warning'><i class="fa fa-exclamation-triangle"></i></span> Data Kemampuan Bahasa Belum Diisi<br>`;
  }

  popoverContent =
    popoverContent === ""
      ? `<span class='text-success'><i class="fa fa-check-square"></i></span>  Data sudah lengkap<br>`
      : popoverContent;

  return totalProgress;
};

const previewCv = (userUrl) => {
  $("#modalPreview").modal("show");
  $("#loader").show();
  $("#modalPreview")
    .find("iframe")
    .attr("src", `/share/${userUrl}?hideHeaderImage=true`);
  $("#modalPreview")
    .find("iframe")
    .on("load", function () {
      $("#loader").hide();
      $("#modalPreview").find("iframe").show();
    });
};

$("#modalPreview").on("hidden.bs.modal", function (e) {
  $("#modalPreview").find("iframe").hide().attr("src", "");
});

function copy() {
  $(".message").text("link disalin");
  let copyText = document.getElementById("inputUrl");
  copyText.select();
  copyText.setSelectionRange(0, 99999);
  navigator.clipboard.writeText(copyText.value);
}
