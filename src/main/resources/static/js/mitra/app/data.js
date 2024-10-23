"use strict";

let messageErrorWhenSaving;

let countHardSkill = 0,
  countSoftSkill = 0,
  countAllSkill;

let softExist = false;
let hardExist = false;
let finishInitialized = false;

$(window).on("load", () => {
  getDataApi(false);

  $("#btnSave").click(($e) => {
    saveData($e);
  });
});

$(document).ready(() => {
  //autoSaveInterval();
  if (readCookie("refreshToken") == "") {
    return;
  }
  autoRefreshToken();
});

const validateMinMaxValue = (element, min, max) => {
  if (element.val() < min || element.val() > max) {
    element.val(0);
    element.trigger("input");
  }
};

const validateYear = (el) => {
  if (el.val().length > 4) {
    el.val(el.val().substr(0, 4));
    el.trigger("keyup");
  }
};

const validateRequire = (el, collapseSelector = "") => {
  el.parent().find(".invalid-feedback").remove();

  el.removeClass("is-invalid");
  if (el.val() == "") {
    el.addClass("is-invalid");
    el.after("<p class='invalid-feedback'>Field ini harus di isi.</p>");
  }
  validateBtnSave();
};

const validatePhotoRequire = (el) => {
  if (el.attr("src") == baseUrl + "/assets/img/no_image.jpg") {
    // el.after("<p class='invalid-feedback'>Field ini harus di isi.</p>")
    el.addClass("is-invalid border border-danger");
    $("#invalidPhoto").show();
  } else {
    el.removeClass("is-invalid border border-danger");
    $("#invalidPhoto").hide();
  }
};

const validateBeforeSave = async () => {
  $("#profilePicturePreview").trigger("change");
  $("#inputNationality").trigger("keyup");
  $("#inputBirthPlace").trigger("keyup");
  $("#selectMarriageStatus").trigger("change");
  $("#selectGender").trigger("change");
  $("#selectReligion").trigger("change");

  let blankField = 0;
  let errors = $("body").find(".invalid-feedback");

  $(".required").each((index, row) => {
    $(row).parent().find(".invalid-feedback").remove();
    $(row).removeClass("is-invalid");

    if ($(row).val() == "" || $(row).val() == null) {
      $(row).addClass("is-invalid");
      $(row).after("<p class='invalid-feedback'>Field ini harus di isi.</p>");
      let collapseSelector = $(row).data("collapse");
      // console.log(collapseSelector)
      $(collapseSelector).collapse("show");
      blankField += 1;
    }

    if (
      $("#profilePicturePreview").attr("src") ==
      baseUrl + "/assets/img/no_image.jpg"
    ) {
      blankField += 1;
    }
  });

  if (blankField > 0) {
    $("html, body").animate(
      {
        scrollTop: $(".is-invalid:first").offset().top - 100,
      },
      1000
    );
    messageErrorWhenSaving = "Periksa kembali form anda!";
    return false;
  }

  if (errors.length > 0) {
    return false;
  }
  if (
    checkIsDataAlreadyExist("educationData") &&
    checkIsDataAlreadyExist("languageData") &&
    checkIsDataAlreadyExist("skillData")
  ) {
    return true;
  } else {
    messageErrorWhenSaving =
      "Masukkan setidaknya 1 Pendidikan, 1 Kemampuan Bahasa, 1 Skill, dan lengkapi Data Pribadi Anda";
    return false;
  }
};

const checkDegree = (el) => {
  let id = el.attr("id").split("--")[1];
  $("#inputEducationMajor--" + id).prop("disabled", false);
  $("#inputEducationGpa--" + id).prop("disabled", false);
  $("#inputEducationMajor--" + id).addClass("required");
  $("#inputEducationGpa--" + id).addClass("required");

  if (el.val() == "SMP") {
    $("#inputEducationMajor--" + id).prop("disabled", true);
    $("#inputEducationGpa--" + id).prop("disabled", true);
    $("#inputEducationMajor--" + id).val("");
    $("#inputEducationGpa--" + id).val("");
    $("#inputEducationMajor--" + id).removeClass("required");
    $("#inputEducationMajor--" + id).prop("required", false);
    $("#inputEducationGpa--" + id).removeClass("required");
    $("#inputEducationGpa--" + id).removeAttr("required");
    $("#inputEducationGpa--" + id)
      .parent()
      .find(".invalid-feedback")
      .remove();
    $("#inputEducationMajor--" + id)
      .parent()
      .find(".invalid-feedback")
      .remove();
    $("#inputEducationGpa--" + id).removeClass("is-invalid");
    $("#inputEducationMajor--" + id).removeClass("is-invalid");
  }

  if (el.val() == "SMA/SMK/MA") {
    $("#inputEducationGpa--" + id).prop("disabled", true);
    $("#inputEducationGpa--" + id).val("");
    $("#inputEducationGpa--" + id).removeClass("required");
    $("#inputEducationGpa--" + id).removeAttr("required");
    $("#inputEducationGpa--" + id)
      .parent()
      .find(".invalid-feedback")
      .remove();
    $("#inputEducationMajor--" + id)
      .parent()
      .find(".invalid-feedback")
      .remove();
    $("#inputEducationGpa--" + id).removeClass("is-invalid");
    $("#inputEducationMajor--" + id).removeClass("is-invalid");
  }
};

const autoSaveInterval = () => {
  window.setInterval(() => {
    saveData();
  }, 50000);
};

const optionSaved = () => {
  iziToast.info({
    timeout: false,
    progressBar: false,
    close: false,
    overlay: true,
    displayMode: "once",
    id: "question",
    zindex: 999,
    title: "Info",
    message: "Tekan Crtl + S untuk menyimpan perubahan",
    position: "topCenter",
    buttons: [
      [
        '<button><b><i class="fa fa-thumbs-up"></i> Saya Mengerti!</b></button>',
        (instance, toast) => {
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");
          $(document).on("keydown", disableF5);
          runSaveMethod();
        },
        true,
      ],
      [
        "<button>Dont show again</button>",
        (instance, toast) => {
          localStorage.setItem("isNotifDontShowAgain", "true");
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");
        },
      ],
    ],
  });
};

$(window).bind("beforeunload", (e) => {
  let isSaved = true;
  if (!isSaved) {
    return "Data will be lost if you leave the page, are you sure?";
    e.preventDefault();
  }
});

const disableF5 = (e) => {
  if ((e.which || e.keyCode) === 116) e.preventDefault();
};

const runSaveMethod = () => {
  jQuery(document).keydown((event) => {
    if ((event.ctrlKey || event.metaKey) && event.which === 83) {
      saveData();
      event.preventDefault();
      return false;
    }
  });
};

const requestRefreshToken = () => {
  return postData("/request-refresh-token", "");
};

const saveData = (e) => {
  validateBeforeSave().then((res) => {
    if (res) {
      // e.preventDefault();
      showLoadingToast("Sedang menyimpan data...");
      $("#btnSave")
        .prop("disabled", true)
        .html('<i class="fas fa-spinner fa-pulse"></i> Sedang Menyimpan');

      if (readCookie("isRedirect") != null && readCookie("isRedirect") == 1) {
        postData(baseUrl + "/refresh-token", "")
          .then((res) => {
            saveAllData(res);
            setCookie("refreshToken", res.message, 30);
          })
          .catch((err) => {
            console.log(err.message);
            errorSaveAllData(e);
          });
      } else {
        const getLocal = localStorage.getItem("personalData");
        const localParse = JSON.parse(getLocal);
        const getEmail = localParse["email"];

        if (getEmail.endsWith("mii.co.id")) {
          saveAllData(res);
        } else {
          requestRefreshToken()
            .then((res) => {
              saveAllData(res);
              setCookie("refreshToken", res.token, 7);
            })
            .catch((err) => {
              console.log(err.message);
              errorSaveAllData(e);
            });
        }
      }
    } else {
      e.preventDefault();
      setDefaultBtnSave();
      iziToast.destroy();
      iziToast.warning({
        title: "Peringatan",
        message: messageErrorWhenSaving,
        position: "topCenter",
      });
    }
  });
};

const saveAllData = () => {
  savePersonalData();
  if (typeof form !== "undefined") {
    blobData("/api/aws/photo/upload", form);
  }
  $.when(
    postData(
      baseUrl + "/api/save-user-language",
      localStorage.getItem("languageData")
    ),
    postData(
      baseUrl + "/api/save-user-info",
      localStorage.getItem("personalData")
    ),
    postData(
      baseUrl + "/api/save-user-education",
      localStorage.getItem("educationData")
    ),
    postData(
      baseUrl + "/api/save-user-skill",
      localStorage.getItem("skillData")
    ),
    postData(
      baseUrl + "/api/save-user-work-assignment",
      localStorage.getItem("workData")
    ),
    postData(
      baseUrl + "/api/save-user-project",
      localStorage.getItem("projectData")
    ),
    postData(
      baseUrl + "/api/save-user-certificate",
      localStorage.getItem("trainingData")
    ),
    postData(
      baseUrl + "/api/save-user-certification",
      localStorage.getItem("certificationData")
    ),
    postData(
      baseUrl + "/api/save-user-organization",
      localStorage.getItem("organizationData")
    ),
    postData(
      baseUrl + "/api/save-user-experience",
      localStorage.getItem("experienceData")
    ),
    postData(
      baseUrl + "/api/save-user-awards",
      localStorage.getItem("awardData")
    )
  )
    .done(() => {
      postData(baseUrl + "/api/notify-rm-me", null)
        .then((res) => {
          $("#frame").contents().find("#imgProfileImagePreview").hide();
          $("#imgProfileImagePreview").hide();
          $("#profilePicturePreview").hide();
          $(".loadingImage").show();
          sessionStorage.setItem("user", btoa(res.message));
          sessionStorage.setItem("idUser", btoa(res.tokenMessage));
          sessionStorage.setItem("fullname", btoa(res.fullNameMessage));
          let successMessage = "Data telah disimpan";
          if (
            readCookie("isRedirect") !== null &&
            readCookie("isRedirect") == 1
          ) {
            successMessage =
              "Data telah disimpan, Anda dapat melanjutkan pendaftaran lowongan melalui <a href='https://recruit-me.metrodataacademy.id' target='_blank'>Recruit.Me</a>";
          }
          iziToast.destroy();
          iziToast.success({
            title: "Berhasil",
            message: successMessage,
            position: "topCenter",
          });
          let photo = $("#profilePicturePreview").attr("src");
          document.cookie = `profilePicture=${photo}; path=/`;

          autoSave();
          getDataApi(true);

          scrollToElement(0);
          updateY();
          setDefaultBtnSave();
          setTimeout(function () {
            location.reload();
          }, 1500);
        })
        .catch((e) => {
          console.error(e);
        });
    })
    .fail((err) => {
      iziToast.destroy();
      iziToast.error({
        title: "Error",
        message: "Terjadi masalah ketika menyimpan data",
        position: "topCenter",
      });
      setDefaultBtnSave();
    });
};

const errorSaveAllData = (e) => {
  e.preventDefault();
  iziToast.destroy();
  iziToast.warning({
    title: "Peringatan",
    message: "Gagal otentikasi, silahkan login kembali!",
    position: "topCenter",
    buttons: [
      [
        '<button><b><i class="fas fa-thumbs-up"></i>Saya Mengerti</b></button>',
        function (instance, toast) {
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");
          window.open(`${baseUrl}`);
        },
        true,
      ],
    ],
  });
  setDefaultBtnSave();
};
const checkIsDataAlreadyExist = (sectionName) => {
  let data = localStorage.getItem(sectionName);
  return data === "[]" || data === "" || data === null || data.length === 0
    ? false
    : true;
};

//Updated
const savePersonalData = () => {
  let personalData = {
    id:$("#talentId").val(),
    fullName: $("#inputFullName").val(),
    photo: $("#profilePicturePreview").prop("src"),
    dateOfBirth: $("#inputBirthDate").val(),
    placeOfBirth: $("#inputBirthPlace").val(),
    nationalityId: $("#inputNationality").val(),
    maritalStatus: $("#selectMarriageStatus").val(),
    gender: $("#selectGender").val(),
    religion: $("#selectReligion").val(),
    phone: $("#inputPhone").val(),
    email: $("#inputEmail").val(),
    cityId: $("#inputCity").val(),
    provinceId: $("#inputProvince").val(),
    fullAddress: $("#inputAddress").val(),
  };
  localStorage.setItem("personalData", JSON.stringify(personalData));
};

//Updated
const setPersonalData = (jsonPersonalData) => {
  $("#inputFullName").val(jsonPersonalData.fullName.toUpperCase()).trigger("input");
  $("#profilePicturePreview").attr(
    "src",
    jsonPersonalData.photo === "" || jsonPersonalData.photo === null
      ? baseUrl + "/assets/img/no_image.jpg"
      : jsonPersonalData.photo
  );
  $("#frame")
    .contents()
    .find("#imgProfileImagePreview")
    .attr(
      "src",
      jsonPersonalData.photo === "" || jsonPersonalData.photo === null
        ? baseUrl + "/assets/img/no_image.jpg"
        : jsonPersonalData.photo
    );
  $("#imgProfileImagePreview").attr(
    "src",
    jsonPersonalData.photo === "" || jsonPersonalData.photo === null
      ? baseUrl + "/assets/img/no_image.jpg"
      : jsonPersonalData.photo
  );
  $("#inputBirthDate").val(jsonPersonalData.dateOfBirth).trigger("change");
  $("#inputBirthPlace").val(jsonPersonalData.placeOfBirth).trigger("keyup");
  $("#inputNationality").val(jsonPersonalData.nationalityId).trigger("keyup");
  $("#selectMarriageStatus")
    .val(jsonPersonalData.maritalStatus)
    .trigger("change");
  $("#selectGender").val(jsonPersonalData.gender).trigger("change");
  $("#selectReligion").val(jsonPersonalData.religion).trigger("change");

  if (jsonPersonalData.email.endsWith("mii.co.id")) {
    $("#inputPhone").attr("type", "text");
    $("#inputPhone")
      .val("+62 21 29 345 777")
      .trigger("input")
      .prop("disabled", true)
      .addClass("disabled-input");
    $("#inputEmail")
      .val("recruitment@mii.co.id")
      .trigger("input")
      .prop("disabled", true)
      .addClass("disabled-input");
    $("#inputCity")
      .val("Jakarta Barat")
      .trigger("keyup")
      .prop("disabled", true)
      .addClass("disabled-input");
    $("#inputProvince")
      .val("DKI Jakarta")
      .trigger("keyup")
      .prop("disabled", true)
      .addClass("disabled-input");
    $("#selectGender")
      .val(jsonPersonalData.gender)
      .prop("disabled", true)
      .wrap('<div class="disabled-select-wrapper"></div>');
    $("#selectMarriageStatus")
      .val(jsonPersonalData.maritalStatus)
      .prop("disabled", true)
      .wrap('<div class="disabled-select-wrapper"></div>');
    $("#inputBirthPlace")
      .val(jsonPersonalData.placeOfBirth)
      .prop("disabled", true)
      .addClass("disabled-input");
    $("#inputAddress")
      .val(
        "PT Mitra Integrasi Informatika APL Tower 37th floor,Suite 1-8 Jl. LetJen S. Parman Kav 28, Tanjung Duren Selatan, Grogol Petamburan"
      )
      .trigger("input")
      .prop("disabled", true)
      .addClass("disabled-input");
    $("#inputFullName").addClass("disabled-input");
    $("#inputBirthDate").addClass("disabled-input");
  } else {
    $("#inputPhone")
      .val(jsonPersonalData.phone.replace(/\+/g, ""))
      .trigger("input");
    $("#inputEmail").val(jsonPersonalData.email).trigger("input");
    $("#inputCity").val(jsonPersonalData.cityId).trigger("keyup");
    $("#inputProvince").val(jsonPersonalData.provinceId).trigger("keyup");
    $("#inputAddress").val(jsonPersonalData.fullAddress).trigger("input");
    $("#selectGender").val(jsonPersonalData.gender).trigger("change");
    $("#selectMarriageStatus")
      .val(jsonPersonalData.maritalStatus)
      .trigger("change");
    $("#inputBirthPlace").val(jsonPersonalData.placeOfBirth).trigger("keyup");
  }
};

//Updated
const saveLanguageData = () => {
  let languageData = [
    ...document.querySelectorAll("[id^=inputLanguageName--]"),
  ].map((input) => {
    return {
      language_id: $(`#inputLanguageId--${input.id.split("--")[1]}`).val(),
      talent_id: $("#talentId").val(),
      readingAbilityLevel: $(`.selectLanguageLevel--${input.id.split("--")[1]}--2`).val(),
      writingAbilityLevel: $(`.selectLanguageLevel--${input.id.split("--")[1]}--1`).val(),
      speakingAbilityLevel: $(`.selectLanguageLevel--${input.id.split("--")[1]}--3`).val(),
    };
  });

  let newLanguageData = languageData.filter((el) => {
    return el.language_id !== "";
  });

  localStorage.setItem("languageData", JSON.stringify(newLanguageData));
};

const setLanguageData = (jsonLanguageData) => {
  const newGroupedData = [];
  const languageMap = new Map();
  jsonLanguageData.forEach((data) => {
    const { language_id, readingAbilityLevel, writingAbilityLevel, speakingAbilityLevel } = data;
    if (!languageMap.has(language_id)) {
      languageMap.set(language_id, { language_id, skills: {} });
    }
    languageMap.get(language_id).skills = {
      Reading: readingAbilityLevel,
      Writing: writingAbilityLevel,
      Speaking: speakingAbilityLevel,
    };
  });
  newGroupedData.push(...languageMap.values());

  $("#sectionLanguage").fadeOut().empty().fadeIn(500);
  $("#txtLanguageProfiency").fadeOut().empty().fadeIn(500);
  $("#frame")
    .contents()
    .find("#txtLanguageProfiency")
    .fadeOut()
    .empty()
    .fadeIn(500);
  countSectionLanguage = 0;

  let index = 1; // Index for language sections

  for (const language of newGroupedData) {
    $("#btnAddSectionLanguage").click();

    $(`#inputLanguageId--${index}`).val(language.language_id).trigger("change");
    $(`#inputLanguageName--${index}`)
      .val(language.language_id) // Assuming you have a way to map language_id to languageName
      .trigger("keyup");
    $(`.selectLanguageLevel--${index}--1`)
      .val(language.skills.Writing)
      .trigger("change");
    $(`.selectLanguageLevel--${index}--2`)
      .val(language.skills.Reading)
      .trigger("change");
    $(`.selectLanguageLevel--${index}--3`)
      .val(language.skills.Speaking)
      .trigger("change");

    index++;
  }
};

//Updated
const saveEducationData = () => {
  let educationData = [
    ...document.querySelectorAll("[id^=inputEducationUniversity--]"),
  ].map((input) => {
    return {
      talent_id: $("#talentId").val(),
      educationalLevel: $(`#selectEducationDegree--${input.id.split("--")[1]}`).val(),
      startDate: $(`#inputEducationStartDate--${input.id.split("--")[1]}`).val(),
      endDate: $(`#inputEducationEndDate--${input.id.split("--")[1]}`).val(),
      academicGrade: parseFloat($(`#inputEducationGpa--${input.id.split("--")[1]}`).val()),
      major_id: $(`#inputEducationMajor--${input.id.split("--")[1]}`).val(),
    };
  });

  let newEducationData = educationData.filter((el) => {
    return el.educationalLevel !== "";
  });

  localStorage.setItem("educationData", JSON.stringify(newEducationData));
};

//Updated
const setEducationData = (jsonEducationData) => {
  $("#frame")
    .contents()
    .find("#txtEducationFrame")
    .fadeOut()
    .empty()
    .fadeIn(500);
  $("#txtEducationFrame").fadeOut().empty().fadeIn(500);
  $("#sectionEducation").fadeOut().empty().fadeIn(500);
  countSectionEducation = 0;

  for (let i = 0; i < jsonEducationData.length; i++) {
    $("#btnAddSectionEducation").click();

    let index = i + 1;
    let enrollElement = $(`#educationUnEnroll--${index}`);

    $(`#inputEducationId--${index}`).val(jsonEducationData[i].talent_id);
    $(`#inputEducationUniversity--${index}`)
      .val(jsonEducationData[i].university)
      .trigger("keyup");
    $(`#selectEducationDegree--${index}`)
      .val(jsonEducationData[i].educationalLevel)
      .trigger("keyup")
      .trigger("change");
    $(`#inputEducationMajor--${index}`)
      .val(jsonEducationData[i].major_id)
      .trigger("keyup");
    $(`#inputEducationGpa--${index}`)
      .val(jsonEducationData[i].academicGrade)
      .trigger("input");
    $(`#inputEducationStartDate--${index}`)
      .val(jsonEducationData[i].startDate)
      .trigger("input");
    $(`#inputEducationEndDate--${index}`)
      .val(jsonEducationData[i].endDate)
      .trigger("input");
    if (
      jsonEducationData[i].endDate == null &&
      jsonEducationData[i].startDate != null
    ) {
      enrollElement.prop("checked", true);
      enrollElement.trigger("change");
    } else {
      $(`#inputEducationEndDate--${index}`)
        .val(jsonEducationData[i].endDate)
        .trigger("input");
      enrollElement.prop("checked", false);
      enrollElement.trigger("change");
    }
  }
};

//Updated
const saveSkillData = () => {
  let skillData = [...document.querySelectorAll("[id^=inputSkillName--]")].map(
    (input) => {
      return {
        talent_id: $("#talentId").val(),
        name: input.value,
        level: $(`#selectSkillLevel--${input.id.split("--")[1]}`).val(),
        category: $(`#selectSkillCategory--${input.id.split("--")[1]}`).val(),
      };
    }
  );

  let newSkillData = skillData.filter((el) => {
    return el.name !== "";
  });

  localStorage.setItem("skillData", JSON.stringify(newSkillData));
};

//Updated
const setSkillData = (jsonSkillData) => {
  hardExist = false;
  softExist = false;
  finishInitialized = false;

  $("#frame").contents().find("#txtSkillFrame").fadeOut().empty().fadeIn(500);
  $("#txtSkillFrame").fadeOut().empty().fadeIn(500);
  $("#sectionSkill").fadeOut().empty().fadeIn(500);
  countSectionSkill = 0;
  countHardSkill = 0;
  countSoftSkill = 0;
  countAllSkill = 0;

  const hardSkill = jsonSkillData.filter((row) => row.category === "HARD_SKILL");
  const softSkill = jsonSkillData.filter((row) => row.category === "SOFT_SKILL");

  countAllSkill = jsonSkillData.length;
  countHardSkill = hardSkill.length;
  let index = 0;

  for (let i = 0; i < hardSkill.length; i++) {
    $("#btnAddSectionSkill").click();

    index++;
    $(`#inputSkillId--${index}`).val(hardSkill[i].talent_id);
    $(`#inputSkillName--${index}`).val(hardSkill[i].name).trigger("keyup");
    $(`#selectSkillLevel--${index}`).val(hardSkill[i].level).trigger("change");
    $(`#selectSkillCategory--${index}`)
      .val(hardSkill[i].category)
      .trigger("change");
  }

  countSoftSkill = softSkill.length;

  for (let i = 0; i < softSkill.length; i++) {
    $("#btnAddSectionSkill").click();

    index++;
    $(`#inputSkillId--${index}`).val(softSkill[i].talent_id);
    $(`#inputSkillName--${index}`).val(softSkill[i].name).trigger("keyup");
    $(`#selectSkillLevel--${index}`).val(softSkill[i].level).trigger("change");
    $(`#selectSkillCategory--${index}`)
      .val(softSkill[i].category)
      .trigger("change");
  }
};

//Updated
const saveWorkData = () => {
  let workData = [...document.querySelectorAll("[id^=inputCompanyName--]")].map(
    (input) => {
      let endDate;
      if ($(`#workUnEnroll--${input.id.split("--")[1]}`).is(":checked")) {
        endDate = null;
      } else {
        endDate = $(`#inputWorkEndDate--${input.id.split("--")[1]}`).val();
      }
      return {
        talent_id: $("#talentId").val(),
        companyName: input.value,
        contractStatus: $(`#selectWorkStatus--${input.id.split("--")[1]}`).val(),
        startDate: $(`#inputWorkStartDate--${input.id.split("--")[1]}`).val(),
        endDate: endDate,
        description: $(`#inputJobDescription--${input.id.split("--")[1]}`).summernote("isEmpty")
          ? null
          : $(`#inputJobDescription--${input.id.split("--")[1]}`).summernote("code"),
        projectSpecification: $(`#inputProjectSpesification--${input.id.split("--")[1]}`).summernote("isEmpty")
          ? null
          : $(`#inputProjectSpesification--${input.id.split("--")[1]}`).summernote("code"),
        position_id: $(`#inputPosition--${input.id.split("--")[1]}`).val(),
      };
    }
  );

  let newWorkData = workData.filter((el) => {
    return el.companyName !== "";
  });

  localStorage.setItem("workData", JSON.stringify(newWorkData));
};

//Updated
const setWorkData = (jsonWorkData) => {
  jsonWorkData.sort((a, b) => new Date(b.startDate) - new Date(a.startDate));

  $("#sectionWork").fadeOut().empty().fadeIn(500);
  $("#txtWorkFrame").fadeOut().empty().fadeIn(500);
  $("#frame").contents().find("#txtWorkFrame").fadeOut().empty().fadeIn();
  countSectionWork = 0;
  workNumber = 0;

  for (let i = 0; i < jsonWorkData.length; i++) {
    $("#btnAddSectionWork").click();

    let index = i + 1;
    $(`#inputWorkId--${index}`).val(jsonWorkData[i].talent_id);
    $(`#inputCompanyName--${index}`)
      .val(jsonWorkData[i].companyName)
      .trigger("input");
    $(`#inputPosition--${index}`)
      .val(jsonWorkData[i].position_id)
      .trigger("keyup");
    $(`#inputWorkStartDate--${index}`)
      .val(jsonWorkData[i].startDate)
      .trigger("input");
    $(`#selectWorkStatus--${index}`)
      .val(jsonWorkData[i].contractStatus)
      .trigger("change");
    $(`#inputJobDescription--${index}`).summernote(
      "code",
      !jsonWorkData[i].description.length
        ? "<ul><li></li></ul>"
        : jsonWorkData[i].description
    );
    $(`#inputProjectSpesification--${index}`).summernote(
      "code",
      !jsonWorkData[i].projectSpecification.length
        ? "<ul><li></li></ul>"
        : jsonWorkData[i].projectSpecification
    );

    let enrollElement = $(`#workUnEnroll--${index}`);
    if (jsonWorkData[i].endDate == null && jsonWorkData[i].startDate != null) {
      enrollElement.prop("checked", true);
      enrollElement.trigger("change");
    } else {
      $(`#inputWorkEndDate--${index}`)
        .val(jsonWorkData[i].endDate)
        .trigger("input");
      enrollElement.prop("checked", false);
      enrollElement.trigger("change");
    }
  }
};

//Updated
const saveProjectData = () => {
  let projectData = [
    ...document.querySelectorAll("[id^=inputProjectName--]"),
  ].map((input) => {
    let startDate, endDate;
    if ($(`#projectUnEnroll--${input.id.split("--")[1]}`).is(":checked")) {
      startDate = null;
      endDate = null;
    } else {
      endDate = $(`#inputProjectEndDate--${input.id.split("--")[1]}`).val();
    }
    startDate = $(`#inputProjectStartDate--${input.id.split("--")[1]}`).val();

    const skill = $(`#inputProjectSkill--${input.id.split("--")[1]}`).val();
    const site = $(`#inputProjectSite--${input.id.split("--")[1]}`).val();

    return {
      talent_id: $("#talentId").val(),
      projectName: input.value,
      projectDescription: $(
        `#inputProjectJobDescription--${input.id.split("--")[1]}`
      ).summernote("isEmpty")
        ? null
        : $(
            `#inputProjectJobDescription--${input.id.split("--")[1]}`
          ).summernote("code"),
      projectSite: site,
      projectStartDate: startDate,
      projectEndDate: endDate,
      projectSkill: skill,
    };
  });

  let newProjectData = projectData.filter((el) => {
    return el.projectName !== "";
  });

  localStorage.setItem("projectData", JSON.stringify(newProjectData));
};

//Updated
const setProjectData = (jsonProjectData) => {
  $("#sectionProject").fadeOut().empty().fadeIn(500);
  $("#txtProjectFrame").fadeOut().empty().fadeIn(500);
  $("#frame").contents().find("#txtProjectFrame").fadeOut().empty().fadeIn();

  countSectionProject = 0;
  for (let i = 0; i < jsonProjectData.length; i++) {
    $("#btnAddSectionProject").click();

    let index = i + 1;
    $(`#inputProjectId--${index}`).val(jsonProjectData[i].talent_id);
    $(`#inputProjectName--${index}`)
      .val(jsonProjectData[i].projectName)
      .trigger("input");
    $(`#inputProjectSite--${index}`)
      .val(jsonProjectData[i].projectSite)
      .trigger("input");
    $(`#inputProjectJobDescription--${index}`).summernote(
      "code",
      !jsonProjectData[i].projectDescription.length
        ? "<ul><li></li></ul>"
        : jsonProjectData[i].projectDescription
    );
    $(`#inputProjectStartDate--${index}`)
      .val(jsonProjectData[i].projectStartDate)
      .trigger("input");
    $(`#inputProjectSkill--${index}`)
      .val(jsonProjectData[i].projectSkill)
      .trigger("input");

    let enrollElement = $(`#projectUnEnroll--${index}`);
    if (
      jsonProjectData[i].projectEndDate == null &&
      jsonProjectData[i].projectStartDate != null
    ) {
      enrollElement.prop("checked", true);
      enrollElement.trigger("change");
    } else {
      $(`#inputProjectEndDate--${index}`)
        .val(jsonProjectData[i].projectEndDate)
        .trigger("input");
      enrollElement.prop("checked", false);
      enrollElement.trigger("change");
    }
  }
};

//Updated
const saveTrainingData = () => {
  let trainingData = [
    ...document.querySelectorAll("[id^=inputTrainingName--]"),
  ].map((input) => {
    return {
      talentId: $("#talentId").val(),
      trainingName: input.value,
      trainingDate: $(`#inputTrainingDate--${input.id.split("--")[1]}`).val(),
      syllabus: $(`#inputTrainingSyllabus--${input.id.split("--")[1]}`).val(),
    };
  });

  let newTrainingData = trainingData.filter((el) => {
    return el.trainingName !== "";
  });

  localStorage.setItem("trainingData", JSON.stringify(newTrainingData));
};

//Updated
const setTrainingData = (jsonTrainingData) => {
  $("#sectionTraining").fadeOut().empty().fadeIn(500);
  $("#txtTrainingFrame").fadeOut().empty().fadeIn(500);
  $("#frame").contents().find("#txtTrainingFrame").fadeOut().empty().fadeIn();
  countSectionTraining = 0;
  for (let i = 0; i < jsonTrainingData.length; i++) {
    $("#btnAddSectionTraining").click();

    let index = i + 1;
    $(`#inputTrainingId--${index}`).val(jsonTrainingData[i].talentId);
    $(`#inputTrainingName--${index}`)
      .val(jsonTrainingData[i].trainingName)
      .trigger("input");
    $(`#inputTrainingDate--${index}`)
      .val(jsonTrainingData[i].trainingDate)
      .trigger("input");
    $(`#inputTrainingSyllabus--${index}`)
      .val(jsonTrainingData[i].syllabus)
      .trigger("input");
  }
};

//Updated
const saveCertificationData = () => {
  let certificationData = [
    ...document.querySelectorAll("[id^=inputCertificationName--]"),
  ].map((input) => {
    let validUntil, certificateIssueDate;
    if (
      $(`#certificationUnEnroll--${input.id.split("--")[1]}`).is(":checked")
    ) {
      validUntil = null;
      certificateIssueDate = null;
    } else {
      certificateIssueDate = $(
        `#inputCertificationReleaseDate--${input.id.split("--")[1]}`
      ).val();
      validUntil = $(
        `#inputCertificationValidityPeriod--${input.id.split("--")[1]}`
      ).val();
    }

    const institutionName = $(
      `#inputCertificationInstitute--${input.id.split("--")[1]}`
    ).val();
    return {
      talentId: $("#talentId").val(),
      certificateName: input.value,
      institutionName: institutionName,
      certificateIssueDate: certificateIssueDate,
      validUntil: validUntil,
    };
  });

  let newCertificationData = certificationData.filter((el) => {
    return el.certificateName !== "";
  });

  localStorage.setItem(
    "certificationData",
    JSON.stringify(newCertificationData)
  );
};

//Updated
const setCertificationData = (jsonCertificationData) => {
  $("#sectionCertification").fadeOut().empty().fadeIn(500);
  $("#txtCertificationFrame").fadeOut().empty().fadeIn(500);
  $("#frame")
    .contents()
    .find("#txtCertificationFrame")
    .fadeOut()
    .empty()
    .fadeIn();

  countSectionCertification = 0;
  for (let i = 0; i < jsonCertificationData.length; i++) {
    $("#btnAddSectionCertification").click();

    let index = i + 1;
    $(`#inputCertificationId--${index}`).val(jsonCertificationData[i].talentId);
    $(`#inputCertificationName--${index}`)
      .val(jsonCertificationData[i].certificateName)
      .trigger("input");
    $(`#inputCertificationInstitute--${index}`)
      .val(jsonCertificationData[i].institutionName)
      .trigger("input");
    $(`#inputCertificationReleaseDate--${index}`)
      .val(jsonCertificationData[i].certificateIssueDate)
      .trigger("input");
    $(`#inputCertificationValidityPeriod--${index}`)
      .val(jsonCertificationData[i].validUntil)
      .trigger("input");

    let enrollElement = $(`#certificationUnEnroll--${index}`);
    if (
      jsonCertificationData[i].validUntil == null &&
      jsonCertificationData[i].certificateIssueDate != null
    ) {
      enrollElement.prop("checked", true);
      enrollElement.trigger("change");
    } else {
      enrollElement.prop("checked", false);
      enrollElement.trigger("change");
    }
  }
};

//Updated
const saveOrganizationData = () => {
  let organizationData = [
    ...document.querySelectorAll("[id^=inputOrganizationName--]"),
  ].map((input) => {
    let endDate;
    if ($(`#organizationUnEnroll--${input.id.split("--")[1]}`).is(":checked")) {
      endDate = null;
    } else {
      endDate = $(
        `#inputOrganizationEndDate--${input.id.split("--")[1]}`
      ).val();
    }
    return {
      talentId: $(`#inputOrganizationId--${input.id.split("--")[1]}`).val(),
      organizationName: input.value,
      organizationPosition: $(
        `#inputOrganizationPosition--${input.id.split("--")[1]}`
      ).val(),
      startDate: $(
        `#inputOrganizationStartDate--${input.id.split("--")[1]}`
      ).val(),
      endDate: endDate,
    };
  });

  let newOrganizationData = organizationData.filter((el) => {
    return el.organizationName !== "";
  });

  localStorage.setItem("organizationData", JSON.stringify(newOrganizationData));
};

//Updated
const setOrganizationData = (jsonOrganizationData) => {
  $("#sectionOrganization").fadeOut().empty().fadeIn(500);
  $("#txtOrganizationFrame").fadeOut().empty().fadeIn(500);
  $("#frame")
    .contents()
    .find("#txtOrganizationFrame")
    .fadeOut()
    .empty()
    .fadeIn();
  countSectionOrganization = 0;
  for (let i = 0; i < jsonOrganizationData.length; i++) {
    $("#btnAddSectionOrganization").click();

    let index = i + 1;
    $(`#inputOrganizationId--${index}`).val(jsonOrganizationData[i].talentId);
    $(`#inputOrganizationName--${index}`)
      .val(jsonOrganizationData[i].organizationName)
      .trigger("input");
    $(`#inputOrganizationPosition--${index}`)
      .val(jsonOrganizationData[i].organizationPosition)
      .trigger("input");
    $(`#inputOrganizationStartDate--${index}`)
      .val(jsonOrganizationData[i].startDate)
      .trigger("input");
    let enrollElement = $(`#organizationUnEnroll--${index}`);
    if (
      jsonOrganizationData[i].endDate == null &&
      jsonOrganizationData[i].startDate != null
    ) {
      enrollElement.prop("checked", true);
      enrollElement.trigger("change");
    } else {
      $(`#inputOrganizationEndDate--${index}`)
        .val(jsonOrganizationData[i].endDate)
        .trigger("input");
      enrollElement.prop("checked", false);
      enrollElement.trigger("change");
    }
  }
};

//Updated
const saveExperienceData = () => {
  let experienceData = [
    ...document.querySelectorAll("[id^=inputExperienceName--]"),
  ].map((input) => {
    return {
      talentId: $(`#inputExperienceId--${input.id.split("--")[1]}`).val(),
      experienceName: input.value,
      instituteName: $(
        `#inputExperienceInstitute--${input.id.split("--")[1]}`
      ).val(),
      experienceDate: $(`#inputExperienceDate--${input.id.split("--")[1]}`).val(),
      positionName: $(`#inputPositionName--${input.id.split("--")[1]}`).val(),
      description: $(`#inputDescriptionName--${input.id.split("--")[1]}`).val(),
    };
  });

  let newExperienceData = experienceData.filter((el) => {
    return el.experienceName !== "";
  });

  localStorage.setItem("experienceData", JSON.stringify(newExperienceData));
};

//Updated
const setExperienceData = (jsonExperienceData) => {
  $("#sectionExperience").fadeOut().empty().fadeIn(500);
  $("#txtExperienceFrame").fadeOut().empty().fadeIn(500);
  $("#frame").contents().find("#txtExperienceFrame").fadeOut().empty().fadeIn();
  countSectionExperience = 0;
  for (let i = 0; i < jsonExperienceData.length; i++) {
    $("#btnAddSectionExperience").click();

    let index = i + 1;
    $(`#inputExperienceId--${index}`).val(jsonExperienceData[i].talentId);
    $(`#inputExperienceName--${index}`)
      .val(jsonExperienceData[i].experienceName)
      .trigger("input");
    $(`#inputExperienceInstitute--${index}`)
      .val(jsonExperienceData[i].instituteName)
      .trigger("input");
    $(`#inputPositionName--${index}`)
      .val(jsonExperienceData[i].positionName)
      .trigger("input");
    $(`#inputDescriptionName--${index}`)
      .val(jsonExperienceData[i].description)
      .trigger("input");
    $(`#inputExperienceDate--${index}`)
      .val(jsonExperienceData[i].experienceDate)
      .trigger("input");
  }
};

//Updated
const saveAchievementData = () => {
  let achievementData = [
    ...document.querySelectorAll("[id^=inputAchievementName--]"),
  ].map((input) => {
    return {
      talent_id: $(`#inputAchievementId--${input.id.split("--")[1]}`).val(),
      achievementName: input.value,
      institution: $(`#inputAchievementInstitute--${input.id.split("--")[1]}`).val(),
      achievementDate: $(`#inputAchievementDate--${input.id.split("--")[1]}`).val(),
    };
  });

  let newAchievementData = achievementData.filter((el) => {
    return el.achievementName !== "";
  });

  localStorage.setItem("achievementData", JSON.stringify(newAchievementData));
};

//Updated
const setAchievementData = (jsonAchievementData) => {
  $("#sectionAchievement").empty().fadeIn(500);
  $("#txtAchievementFrame").empty().fadeIn(500);
  $("#frame").contents().find("#txtAchievementFrame").fadeOut().empty().fadeIn();
  countSectionAchievement = 0;
  for (let i = 0; i < jsonAchievementData.length; i++) {
    $("#btnAddSectionAchievement").click();

    let index = i + 1;
    $(`#inputAchievementId--${index}`).val(jsonAchievementData[i].talent_id);
    $(`#inputAchievementName--${index}`)
      .val(jsonAchievementData[i].achievementName)
      .trigger("keyup");
    $(`#inputAchievementInstitute--${index}`)
      .val(jsonAchievementData[i].institution)
      .trigger("keyup");
    $(`#inputAchievementDate--${index}`)
      .val(jsonAchievementData[i].achievementDate)
      .trigger("input");
  }
};

//Updated
const setDefaultBtnSave = () => {
  $("#btnSave")
    .prop("disabled", false)
    .html('<i class="fa fa-save"></i>  Simpan Perubahan')
    .off("click")
    .on("click", () => {
      const personalData = JSON.parse(localStorage.getItem("personalData"));
      const languageData = JSON.parse(localStorage.getItem("languageData"));
      const educationData = JSON.parse(localStorage.getItem("educationData"));
      const skillData = JSON.parse(localStorage.getItem("skillData"));
      const workData = JSON.parse(localStorage.getItem("workData"));
      const projectData = JSON.parse(localStorage.getItem("projectData"));
      const trainingData = JSON.parse(localStorage.getItem("trainingData"));
      const certificationData = JSON.parse(localStorage.getItem("certificationData"));
      const organizationData = JSON.parse(localStorage.getItem("organizationData"));
      const experienceData = JSON.parse(localStorage.getItem("experienceData"));
      const achievementData = JSON.parse(localStorage.getItem("achievementData"));

      const data = {
        id: personalData.id,
        fullName: personalData.fullName,
        nik: personalData.nik,
        dateOfBirth: personalData.dateOfBirth,
        placeOfBirth: personalData.placeOfBirth,
        nationalityId: personalData.nationalityId,
        maritalStatus: personalData.maritalStatus,
        gender: personalData.gender,
        religion: personalData.religion,
        phone: personalData.phone,
        email: personalData.email,
        provinceId: personalData.provinceId,
        cityId: personalData.cityId,
        fullAddress: personalData.fullAddress,
        languageSkills: languageData,
        educations: educationData,
        skills: skillData,
        jobHistory: workData,
        projects: projectData,
        trainings: trainingData,
        certifications: certificationData,
        organizations: organizationData,
        otherExperience: experienceData,
        achievements: achievementData
      };

      // Log the data to the console
      console.log("Data to be sent:", data);

      // Uncomment the following block to send the data
      /*
      $.ajax({
        url: "/saveData", // Replace with your actual endpoint
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: (response) => {
          console.log("Data saved successfully:", response);
        },
        error: (error) => {
          console.error("Error saving data:", error);
        }
      });
      */
    });
};