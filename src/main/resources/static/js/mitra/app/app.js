let languageY = 0;
let educationY = 0;
let skillY = 0;
let workY = 0;
let projectY = 0;
let trainingY = 0;
let certificationY = 0;
let organizationY = 0;
let experienceY = 0;
let awardY = 0;
let myIframe = document.getElementById("frame");

const updateY = () => {
  languageY =
    myIframe.contentWindow.document
      .getElementById("txtLanguageProfiency")
      .getBoundingClientRect().top - 300;
  educationY =
    myIframe.contentWindow.document
      .getElementById("txtEducationFrame")
      .getBoundingClientRect().top - 300;
  skillY =
    myIframe.contentWindow.document
      .getElementById("txtSkillFrame")
      .getBoundingClientRect().top - 300;
  workY =
    myIframe.contentWindow.document
      .getElementById("txtWorkFrame")
      .getBoundingClientRect().top - 300;
  projectY =
    myIframe.contentWindow.document
      .getElementById("txtProjectFrame")
      .getBoundingClientRect().top - 300;
  trainingY =
    myIframe.contentWindow.document
      .getElementById("txtTrainingFrame")
      .getBoundingClientRect().top - 300;
  certificationY =
    myIframe.contentWindow.document
      .getElementById("txtCertificationFrame")
      .getBoundingClientRect().top - 300;
  organizationY =
    myIframe.contentWindow.document
      .getElementById("txtOrganizationFrame")
      .getBoundingClientRect().top - 300;
  experienceY =
    myIframe.contentWindow.document
      .getElementById("txtExperienceFrame")
      .getBoundingClientRect().top - 300;
  awardY =
    myIframe.contentWindow.document
      .getElementById("txtAwardFrame")
      .getBoundingClientRect().top - 300;
};
const scrollToElement = (y) => {
  myIframe.contentWindow.scrollTo({
    top: y,
    behavior: "smooth",
  });
};

const autoRefreshToken = () => {
  if (readCookie("isRedirect") != null && readCookie("isRedirect") == 1) {
    setInterval(() => {
      postData(baseUrl + "/refresh-token", "")
        .then((response) => {
          if (response.message != null || response.message !== "") {
            setCookie("oldToken", readCookie("refreshToken"), 30);
            setCookie("refreshToken", response.message, 30);
          }
        })
        .catch((err) => {
          console.log(err);
          clearInterval(autoRefreshToken);
        });
    }, 600000);
  } else {
    requestRefreshToken()
      .then((res) => {
        setCookie("refreshToken", res.token, 7);
      })
      .catch((err) => {
        clearInterval(autoRefreshToken);
      });
  }
};

// const scrollToEditedSection = (element) => {
//     const y = element === "txtPersonalSection" ? 0 : myIframe.contentWindow.document.getElementById(element).getBoundingClientRect().top;
//     console.log(y)
//     myIframe.contentWindow.scrollTo({
//         top: y,
//         behavior: "smooth"
//     });
// }

// $("body").on("click", "#personalSection select", () => {
//     scrollToElement(0)
// });

// $("body").on("click", "#personalSection input", () => {
$("#personalSection").click(() => {
  scrollToElement(0);
});

// $("body").on("click", "#sectionLanguage input", () => {
$("#sectionLanguage").click(() => {
  scrollToElement(languageY);
});

// $("body").on("click", "#sectionEducation input", () => {
$("#sectionEducation").click(() => {
  scrollToElement(educationY);
});

// $("body").on("click", "#sectionSkill input", () => {
$("#sectionSkill").click(() => {
  scrollToElement(skillY);
});

// $("body").on("click", "#sectionWork input", () => {
$("#sectionWork").click(() => {
  scrollToElement(workY);
});

// $("body").on("click", "#sectionProject input", () => {
$("#sectionProject").click(() => {
  scrollToElement(projectY);
});

// $("body").on("click", "#sectionTraining input", () => {
$("#sectionTraining").click(() => {
  scrollToElement(trainingY);
});

// $("body").on("click", "#sectionCertification input", () => {
$("#sectionCertification").click(() => {
  scrollToElement(certificationY);
});

// $("body").on("click", "#sectionOrganization input", () => {
$("#sectionOrganization").click(() => {
  scrollToElement(organizationY);
});

// $("body").on("click", "#sectionExperience input", () => {
$("#sectionExperience").click(() => {
  scrollToElement(experienceY);
});

// $("body").on("click", "#sectionAward input", () => {
$("#sectionAward").click(() => {
  scrollToElement(awardY);
});

const shimmerLoading = (selector) => {
  let el = `<div class="loading-card br">
                    <div class="shimmer">
                        <div class="comment br animate w80"></div>
                        <div class="comment br animate"></div>
                        <div class="comment br animate"></div>
                    </div>
                <div>`;

  for (let i = 0; i < selector.length; i++) {
    $(selector[i]).fadeOut().empty().fadeIn();
  }

  for (let i = 0; i < selector.length; i++) {
    $(selector[i]).append(el).hide().fadeIn(2000);
  }
};

const readCookie = (name) => {
  var nameEQ = name + "=";
  var ca = document.cookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == " ") c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
  }
  return null;
};

const setCookie = (name, value, days) => {
  let expires = "";
  if (days) {
    var date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "") + expires + "; path=/";
};

const waitForEl = (selector, callback) => {
  if ($(selector).length) {
    callback();
  } else {
    setTimeout(function () {
      waitForEl(selector, callback);
    }, 100);
  }
};

const autoSave = () => {
  $(document).ready(function () {
    savePersonalData();
    saveLanguageData();
    saveEducationData();
    saveSkillData();
    saveWorkData();
    saveProjectData();
    saveTrainingData();
    saveCertificationData();
    saveOrganizationData();
    saveExperienceData();
    saveAwardData();
    validateBtnSave();
  });
};

const validateBtnSave = () => {
  $("#btnSave").parent().find(".invalid-feedback").remove();
  let errorTotal = $("body").find(".invalid-feedback");
  if (errorTotal.length > 0) {
    $("#btnSave").after(
      "<p class='invalid-feedback text-right mb-2' style='display: block; float: right; font-size: 16px; font-weight: bold'>Periksa Kembali Formulir Anda.</p>"
    );
    //        $("#btnSave").prop("disabled", true);
    var invalidFeedbackElement = $("body")
      .find(".collapse")
      .find(".invalid-feedback")
      .get(0);
    if (invalidFeedbackElement) {
      if (!$(invalidFeedbackElement).closest(".collapse").hasClass("show")) {
        if (!$("body").find(".collapse").find("input:disabled").get(0)) {
          $(invalidFeedbackElement).closest(".collapse").collapse("show");
        }
      }
      invalidFeedbackElement.scrollIntoView({
        behavior: "smooth",
        block: "center",
      });
    }
    $("body").find(".invalid-feedback").get(0)
      ? $("body")
          .find(".invalid-feedback")
          .get(0)
          .scrollIntoView({ behavior: "smooth", block: "center" })
      : "";
  } else {
    $("#btnSave").parent().remove(".invalid-feedback");
    $("#btnSave").prop("disabled", false);
  }
};

const getDataApi = (afterSave) => {
  const selector = [
    "#sectionLanguage",
    "#sectionSkill",
    "#sectionEducation",
    "#sectionWork",
    "#sectionProject",
    "#sectionTraining",
    "#sectionCertification",
    "#sectionOrganization",
    "#sectionExperience",
    "#sectionAward",
  ];

  shimmerLoading(selector);

  if (!afterSave) {
    // showLoadingToast("Mohon tunggu..")
  }

  setTimeout(() => {
    $.when(
      getPersonalDataAPI(),
      getLanguageDataAPI(),
      getSkillDataAPI(),
      getEducationDataAPI(),
      getWorkDataAPI(),
      getProjectDataAPI(),
      getTrainingDataAPI(),
      getCertificationDataAPI(),
      getOrganizationDataAPI(),
      getExperienceDataAPI(),
      getAwardDataAPI()
    ).done((res) => {
      runAutocompleteInitialize().then(() => {
        renderAutocomplete();
      });

      $(".invalid-feedback").remove();
      $(".is-invalid").removeClass("is-invalid");
      $("#btnSave").prop("disabled", false);

      //            $("#personalSectionPlaceholder").fadeOut(() => {
      //                $("#personalSectionPlaceholder").remove()
      //                $("#personalSection").fadeIn();
      //            })

      // if (localStorage.getItem('isNotifDontShowAgain') !== 'true') {
      //     optionSaved();
      // } else {
      //     runSaveMethod();
      // }

      // iziToast.destroy();

      updateY();

      startIntrorial();
    });
  }, 0);
};

const startIntrorial = () => {
  if (readCookie("introrial") == null) {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
    setTimeout(() => {
      introJs()
        .setOptions({
          nextLabel: "Lanjut",
          prevLabel: "Kembali",
          doneLabel: "Selesai",
          scrollToElement: false,
          steps: [
            {
              title: "Halo!",
              intro:
                "Selamat datang di CV-ME, berikut adalah panduan sederhana bagaimana cara membuat CV",
            },
            {
              element: document.querySelector("#cv-editor"),
              title: "Form Input",
              position: "right",
              intro:
                "Dibagian ini Anda dapat melengkapi data informasi mengenai Anda",
            },
            {
              element: document.querySelector("#frame"),
              title: "Preview CV",
              intro:
                "Sambil melengkapi data, Anda juga dapat melihat secara langsung tampilan dari CV Anda",
            },
            {
              element: document.querySelector("#btnSave"),
              title: "Tombol Simpan",
              intro:
                "Setelah selesai melengkapi data, Anda wajib menyimpan perubahan pada CV Anda",
            },
            {
              element: document.querySelector("#btnPreview"),
              title: "Tombol Preview",
              intro:
                "Klik tombol berikut jika Anda ingin melihat secara penuh tampilan CV Anda",
            },
            {
              element: document.querySelector("#btnDownloadCv"),
              title: "Tombol Download",
              intro:
                "Anda juga dapat mendownload CV Anda, hasil download berupa file .pdf atau .word",
            },
            {
              element: document.querySelector("#btnShare"),
              title: "Tombol Share",
              intro:
                "Selain itu, Anda juga bisa membagikan CV Anda baik ke sosial media, maupun berupa link",
            },
          ],
        })
        .onexit(function () {
          setCookie("introrial", 1, 30);
          window.scrollTo({
            top: 0,
            behavior: "smooth",
          });
        })
        .start();
    }, 1000);
  }
};

const getPersonalDataAPI = () => {
  return getData(baseUrl + "/api/get-user-info").then((res) => {
    setPersonalData(res);
  });
};

const getLanguageDataAPI = () => {
  return getData(baseUrl + "/api/get-user-language").then((res) => {
    setLanguageData(res);
  });
};

const getSkillDataAPI = () => {
  return getData(baseUrl + "/api/get-user-skill").then((res) => {
    setSkillData(res);
  });
};

function getEducationDataAPI() {
  return getData(baseUrl + "/api/get-user-education").then((res) => {
    setEducationData(res);
  });
}

const getWorkDataAPI = () => {
  return getData(baseUrl + "/api/get-user-work-assignment").then((res) => {
    setWorkData(res);
  });
};

const getProjectDataAPI = () => {
  return getData(baseUrl + "/api/get-user-project").then((res) => {
    setProjectData(res);
  });
};

const getTrainingDataAPI = () => {
  return getData(baseUrl + "/api/get-user-certificate").then((res) => {
    setTrainingData(res);
  });
};

const getCertificationDataAPI = () => {
  return getData(baseUrl + "/api/get-user-certification").then((res) => {
    setCertificationData(res);
  });
};

const getOrganizationDataAPI = () => {
  return getData(baseUrl + "/api/get-user-organization").then((res) => {
    setOrganizationData(res);
  });
};

const getExperienceDataAPI = () => {
  return getData(baseUrl + "/api/get-user-experience").then((res) => {
    setExperienceData(res);
  });
};

const getAwardDataAPI = () => {
  return getData(baseUrl + "/api/get-user-award").then((res) => {
    setAwardData(res);
  });
};

function copy() {
  $(".message").text("link disalin");
  let copyText = document.getElementById("inputUrl");
  copyText.select();
  copyText.setSelectionRange(0, 99999); /* For mobile devices */
  navigator.clipboard.writeText(copyText.value);
}

function setAddress() {
  if (
    $("#inputAddress").val() === "" &&
    $("#inputCity").val() === "" &&
    $("#inputProvince").val() === ""
  ) {
    $("#frame")
      .contents()
      .find("#txtAddress")
      .text("________________")
      .fadeTo(100, 0.3, function () {
        $(this).fadeTo(100, 1.0);
      });
    $("#txtAddress")
      .text("________________")
      .fadeTo(100, 0.3, function () {
        $(this).fadeTo(100, 1.0);
      });
  } else {
    let address =
      $("#inputAddress").val() === "" ? "" : $("#inputAddress").val() + ", ";
    let city = $("#inputCity").val() === "" ? "" : $("#inputCity").val() + ", ";
    let province = $("#inputProvince").val();
    $("#frame")
      .contents()
      .find("#txtAddress")
      .text(address + city + province)
      .fadeTo(100, 0.3, function () {
        $(this).fadeTo(100, 1.0);
      });
    $("#txtAddress")
      .text(address + city + province)
      .fadeTo(100, 0.3, function () {
        $(this).fadeTo(100, 1.0);
      });
  }
}

// untuk mengubah tampilan ketika date disable / mengaktifkan centang berlaku seumur hidup & selamanya
const disableEndDate = ($e, el, type) => {
  if ($e.is(":checked")) {
    $(el).prop("disabled", true);
    $(el).removeClass("required");
    $(el).removeClass("is-invalid");
    $(el).parent().find(".invalid-feedback").remove();

    validateBtnSave();

    if (type == "education") {
      //            $(el).prop("disabled", false);
      //            $(el).addClass("required")
      //            $(el).removeClass("is-invalid")
      //            $(el).parent().find(".invalid-feedback").remove()
      //            let id = el.split("--")[1];
      //            $(`#inputEducationEndDate--${id}`).parent().find('label').text("Estimasi Kelulusan")
      $(el).val("");
      let id = el.split("--")[1];
      $(el)
        .val("")
        .prop("disabled", true)
        .removeClass("required")
        .removeClass("is-invalid")
        .parent()
        .find(".invalid-feedback")
        .remove();
      $(`#txtEducationEndDate--${id}`).text(" - Current)");
      $("#frame")
        .contents()
        .find(`#txtEducationEndDateFrame--${id}`)
        .text(" - Current)");
    }

    if (type == "work") {
      $(el).val("");
      let id = el.split("--")[1];
      $(el)
        .val("")
        .prop("disabled", true)
        .removeClass("required")
        .removeClass("is-invalid")
        .parent()
        .find(".invalid-feedback")
        .remove();
      $(`#txtWorkEndDate--${id}`).text(" - Present");
      $("#frame")
        .contents()
        .find(`#txtWorkEndDateFrame--${id}`)
        .text(" - Present");
    }

    if (type == "organization") {
      $(el).val("");
      let id = el.split("--")[1];
      $(el)
        .val("")
        .prop("disabled", true)
        .removeClass("required")
        .removeClass("is-invalid");
      $(`#txtOrganizationEndDate--${id}`).text(" - Present"); // di tampilkan pada bagian atas section
      $("#frame")
        .contents()
        .find(`#txtOrganizationEndDateFrame--${id}`)
        .text(" - Present"); // di tampilkan di live preview cv sebelah kanan
      $(`#txtOrganizationEndDateFrame--${id}`).text(" - Present"); // di tampilkan saat tombol Preview CV diklik
    }

    if (type == "project") {
      $(el).val("");
      let id = el.split("--")[1];
      $(el)
        .val("")
        .prop("disabled", true)
        .removeClass("required")
        .removeClass("is-invalid");
      $(`#txtProjectEndDate--${id}`).text(" - Present"); // di tampilkan pada bagian atas section
      $("#frame")
        .contents()
        .find(`#txtProjectEndDateFrame--${id}`)
        .text(" - Present"); // di tampilkan di live preview cv sebelah kanan
      $(`#txtProjectEndDateFrame--${id}`).text(" - Present"); // di tampilkan saat tombol Preview CV diklik
    }

    if (type == "certification") {
      $(el).val("");
      let id = el.split("--")[1];
      $(el)
        .val("")
        .prop("disabled", true)
        .removeClass("required")
        .removeClass("is-invalid");
      // .parent().find(".invalid-feedback").remove();
      $(`#txtCertificationDate--${id}`).text(" - For a Lifetime"); // di tampilkan pada bagian atas section
      $("#frame")
        .contents()
        .find(`#txtCertificationDateFrame--${id}`)
        .text(" - For a Lifetime"); // di tampilkan di live preview cv sebelah kanan
      $(`#txtCertificationDateFrame--${id}`).text(" - For a Lifetime"); // di tampilkan saat tombol Preview CV diklik
    }
  } else {
    //        if (type == 'education') {
    //            let id = el.split("--")[1];
    //            $(`#inputEducationEndDate--${id}`).parent().find('label').text("Berakhir pada")
    // $(`#txtEducationEndDate--${id}`).text(" - Sekarang")
    // $('#frame').contents().find(`#txtEducationEndDateFrame--${id}`).text(" - Sekarang")
    //        }

    $(el).prop("disabled", false);
    $(el).trigger("input");
    $(el).addClass("required");
  }

  autoSave();
};
