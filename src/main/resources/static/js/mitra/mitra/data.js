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
     //    if (readCookie("refreshToken") == "") {
     //        return;
     //    }
     //    autoRefreshToken()
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
     if (el.attr("src") == baseUrl + "/img/no_image.jpg") {
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
               $(collapseSelector).collapse("show");
               blankField += 1;
          }

          if ($("#profilePicturePreview").attr("src") == baseUrl + "/img/no_image.jpg") {
               blankField += 1;
          }
     });

     if (blankField > 500) {
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
     if (checkIsDataAlreadyExist("educationData") && checkIsDataAlreadyExist("languageData") && checkIsDataAlreadyExist("skillData")) {
          return true;
     } else {
          messageErrorWhenSaving = "Masukkan setidaknya 1 Pendidikan, 1 Kemampuan Bahasa, 1 Skill, dan lengkapi Data Pribadi Anda";
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

//const requestRefreshToken = () => {
//    return postData("/request-refresh-token", "");
//}

const saveData = (e) => {
     validateBeforeSave().then((res) => {
          if (res) {
               // e.preventDefault();
               showLoadingToast("Sedang menyimpan data...");
               $("#btnSave").prop("disabled", true).html('<i class="fas fa-spinner fa-pulse"></i> Sedang Menyimpan');

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
                    const getLocal = sessionStorage.getItem("personalData");
                    const localParse = JSON.parse(getLocal);
                    const getEmail = localParse["email"];

                    if (getEmail.endsWith("mii.co.id")) {
                         saveAllData(res);
                    } else {
                         //                    requestRefreshToken().then((res) => {
                         saveAllData(res);
                         //                        setCookie("refreshToken", res.token, 7)
                         //                    }).catch((err) => {
                         //                        console.log(err.message)
                         //                        errorSaveAllData(e);
                         //                    })
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
     $.when(postData(baseUrl + "/api/pickme/save-user-info?hhUserId=" + hhUserId, sessionStorage.getItem("personalData")))
          .done((res) => {
               if (typeof form !== "undefined") {
                    blobData(baseUrl + "/api/aws/photo/upload?hhUserId=" + res.message, form);
               }
               $.when(
                    postData(baseUrl + "/api/pickme/save-user-language?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("languageData")),
                    postData(baseUrl + "/api/pickme/save-user-education?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("educationData")),
                    postData(baseUrl + "/api/pickme/save-user-skill?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("skillData")),
                    postData(baseUrl + "/api/pickme/save-user-work-assignment?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("workData")),
                    postData(baseUrl + "/api/pickme/save-user-project?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("projectData")),
                    postData(baseUrl + "/api/pickme/save-user-certificate?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("trainingData")),
                    postData(baseUrl + "/api/pickme/save-user-certification?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("certificationData")),
                    postData(baseUrl + "/api/pickme/save-user-organization?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("organizationData")),
                    postData(baseUrl + "/api/pickme/save-user-experience?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("experienceData")),
                    postData(baseUrl + "/api/pickme/save-user-awards?hhId=" + hhId + "&hhUserId=" + res.message, sessionStorage.getItem("awardData"))
               ).done(() => {
                    $("#frame").contents().find("#imgProfileImagePreview").hide();
                    $("#imgProfileImagePreview").hide();
                    $("#profilePicturePreview").hide();
                    $(".loadingImage").show();
                    sessionStorage.setItem("user", btoa(res.message));
                    sessionStorage.setItem("idUser", btoa(res.tokenMessage));
                    sessionStorage.setItem("fullname", btoa(res.fullNameMessage));
                    hhUserId = res.message;
                    let successMessage = "Data telah disimpan";
                    if (readCookie("isRedirect") !== null && readCookie("isRedirect") == 1) {
                         successMessage = "Data telah disimpan, Anda dapat melanjutkan pendaftaran lowongan melalui <a href='https://recruit-me.metrodataacademy.id' target='_blank'>Recruit.Me</a>";
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
     let data = sessionStorage.getItem(sectionName);
     return data === "[]" || data === "" || data === null || data.length === 0 ? false : true;
};

const savePersonalData = () => {
     let personalData = {
          hhId: hhId,
          ktp: $("#inputKtp").val(),
          name: $("#inputFullName").val(),
          photo: $("#profilePicturePreview").prop("src"),
          birthDate: $("#inputBirthDate").val(),
          birthPlace: $("#inputBirthPlace").val(),
          nationality: $("#inputNationality").val(),
          marriageStatus: $("#selectMarriageStatus").val(),
          gender: $("#selectGender").val(),
          religion: $("#selectReligion").val(),
          phone: $("#inputPhone").val(),
          email: $("#inputEmail").val(),
          city: $("#inputCity").val(),
          province: $("#inputProvince").val(),
          address: $("#inputAddress").val(),
     };
     //    var hhUserId = sessionStorage.getItem('hhUserId');

     sessionStorage.setItem("personalData", JSON.stringify(personalData));
};

const setPersonalData = (jsonPersonalData) => {
     $("#inputFullName").val(jsonPersonalData.name.toUpperCase()).trigger("input");
     $("#inputKtp").val(jsonPersonalData.ktp).trigger("input");
     $("#profilePicturePreview").attr("src", jsonPersonalData.photo === "" || jsonPersonalData.photo === null ? baseUrl + "/assets/img/no_image.jpg" : jsonPersonalData.photo);
     $("#frame")
          .contents()
          .find("#imgProfileImagePreview")
          .attr("src", jsonPersonalData.photo === "" || jsonPersonalData.photo === null ? baseUrl + "/assets/img/no_image.jpg" : jsonPersonalData.photo);
     $("#imgProfileImagePreview").attr("src", jsonPersonalData.photo === "" || jsonPersonalData.photo === null ? baseUrl + "/assets/img/no_image.jpg" : jsonPersonalData.photo);
     $("#inputBirthDate").val(jsonPersonalData.birthDate).trigger("change");
     $("#inputBirthPlace").val(jsonPersonalData.birthPlace).trigger("keyup");
     $("#inputNationality").val(jsonPersonalData.nationality).trigger("keyup");
     $("#selectMarriageStatus").val(jsonPersonalData.marriageStatus).trigger("change");
     $("#selectGender").val(jsonPersonalData.gender).trigger("change");
     $("#selectReligion").val(jsonPersonalData.religion).trigger("change");

     if (jsonPersonalData.email.endsWith("mii.co.id")) {
          $("#inputPhone").attr("type", "text");
          $("#inputPhone").val("+62 21 29 345 777").trigger("input").prop("disabled", true).addClass("disabled-input");
          $("#inputEmail").val("recruitment@mii.co.id").trigger("input").prop("disabled", true).addClass("disabled-input");
          $("#inputCity").val("Jakarta Barat").trigger("keyup").prop("disabled", true).addClass("disabled-input");
          $("#inputProvince").val("DKI Jakarta").trigger("keyup").prop("disabled", true).addClass("disabled-input");
          $("#selectGender").val(jsonPersonalData.gender).prop("disabled", true).wrap('<div class="disabled-select-wrapper"></div>');
          $("#selectMarriageStatus").val(jsonPersonalData.marriageStatus).prop("disabled", true).wrap('<div class="disabled-select-wrapper"></div>');
          $("#inputBirthPlace").val(jsonPersonalData.birthPlace).prop("disabled", true).addClass("disabled-input");
          $("#inputAddress").val("PT Mitra Integrasi Informatika APL Tower 37th floor,Suite 1-8 Jl. LetJen S. Parman Kav 28, Tanjung Duren Selatan, Grogol Petamburan").trigger("input").prop("disabled", true).addClass("disabled-input");
          $("#inputFullName").addClass("disabled-input");
          $("#inputBirthDate").addClass("disabled-input");
     } else {
          $("#inputPhone").val(jsonPersonalData.phone.replace(/\+/g, "")).trigger("input");
          $("#inputEmail").val(jsonPersonalData.email).trigger("input");
          $("#inputCity").val(jsonPersonalData.city).trigger("keyup");
          $("#inputProvince").val(jsonPersonalData.province).trigger("keyup");
          $("#inputAddress").val(jsonPersonalData.address).trigger("input");
          $("#selectGender").val(jsonPersonalData.gender).trigger("change");
          $("selectMarriageStatus").val(jsonPersonalData.marriageStatus).trigger("change");
          $("#inputBirthPlace").val(jsonPersonalData.birthPlace).trigger("keyup");
     }
};

const saveLanguageData = () => {
     let languageData = [...document.querySelectorAll("[id^=inputLanguageName--]")].map((input) => {
          // const id = $(`#inputLanguageId--${input.id.split('--')[1]}`).val();

          return {
               language_id: $(`#inputLanguageId--${input.id.split("--")[1]}`).val(),
               name: input.value,
               skills: [
                    {
                         competence: "Writing",
                         level: $(`.selectLanguageLevel--${input.id.split("--")[1]}--1`).val(),
                    },
                    {
                         competence: "Reading",
                         level: $(`.selectLanguageLevel--${input.id.split("--")[1]}--2`).val(),
                    },
                    {
                         competence: "Speaking",
                         level: $(`.selectLanguageLevel--${input.id.split("--")[1]}--3`).val(),
                    },
               ],
          };
     });

     let newLanguageData = languageData.filter((el) => {
          return el.name !== "";
     });

     sessionStorage.setItem("languageData", JSON.stringify(newLanguageData));
};

const setLanguageData = (jsonLanguageData) => {
     const newGroupedData = [];
     const languageMap = new Map();
     jsonLanguageData.forEach((data) => {
          const { languageName, languageCompetence, languageLevel } = data;
          if (!languageMap.has(languageName)) {
               languageMap.set(languageName, { languageName, skills: [] });
          }
          languageMap.get(languageName).skills[languageCompetence] = languageLevel;
     });
     newGroupedData.push(...languageMap.values());

     $("#sectionLanguage").fadeOut().empty().fadeIn(500);
     $("#txtLanguageProfiency").fadeOut().empty().fadeIn(500);
     $("#frame").contents().find("#txtLanguageProfiency").fadeOut().empty().fadeIn(500);
     countSectionLanguage = 0;

     let index = 1; // Index for language sections

     for (const language of newGroupedData) {
          $("#btnAddSectionLanguage").click();

          $(`#inputLanguageId--${index}`).val(language.id).trigger("change");
          $(`#inputLanguageName--${index}`).val(language.languageName).trigger("keyup");
          $(`.selectLanguageLevel--${index}--1`).val(language.skills.Writing).trigger("change");
          $(`.selectLanguageLevel--${index}--2`).val(language.skills.Reading).trigger("change");
          $(`.selectLanguageLevel--${index}--3`).val(language.skills.Speaking).trigger("change");

          index++;
     }
};

const saveEducationData = () => {
     let educationData = [...document.querySelectorAll("[id^=inputEducationUniversity--]")].map((input) => {
          return {
               id: $(`#inputEducationId--${input.id.split("--")[1]}`).val(),
               university: input.value,
               degree: $(`#selectEducationDegree--${input.id.split("--")[1]}`).val(),
               major: $(`#inputEducationMajor--${input.id.split("--")[1]}`).val(),
               isGraduated: $(`#educationUnEnroll--${input.id.split("--")[1]}`).is(":checked") ? false : true,
               gpa: $(`#inputEducationGpa--${input.id.split("--")[1]}`).val(),
               startDate: $(`#inputEducationStartDate--${input.id.split("--")[1]}`).val(),
               endDate: $(`#inputEducationEndDate--${input.id.split("--")[1]}`).val(),
          };
     });

     let newEducationData = educationData.filter((el) => {
          return el.university !== "";
     });

     sessionStorage.setItem("educationData", JSON.stringify(newEducationData));
};

const setEducationData = (jsonEducationData) => {
     $("#frame").contents().find("#txtEducationFrame").fadeOut().empty().fadeIn(500);
     $("#txtEducationFrame").fadeOut().empty().fadeIn(500);
     $("#sectionEducation").fadeOut().empty().fadeIn(500);
     countSectionEducation = 0;

     for (let i = 0; i < jsonEducationData.length; i++) {
          $("#btnAddSectionEducation").click();

          let index = i + 1;
          let enrollElement = $(`#educationUnEnroll--${index}`);

          $(`#inputEducationId--${index}`).val(jsonEducationData[i].id);
          $(`#inputEducationUniversity--${index}`).val(jsonEducationData[i].university).trigger("keyup");
          $(`#selectEducationDegree--${index}`).val(jsonEducationData[i].degree).trigger("keyup").trigger("change");
          $(`#inputEducationMajor--${index}`).val(jsonEducationData[i].major).trigger("keyup");
          $(`#inputEducationGpa--${index}`).val(jsonEducationData[i].gpa).trigger("input");
          $(`#inputEducationStartDate--${index}`).val(jsonEducationData[i].startDate).trigger("input");
          $(`#inputEducationEndDate--${index}`).val(jsonEducationData[i].endDate).trigger("input");

          //        if (!jsonEducationData[i].isGraduated) {
          //            enrollElement.prop("checked", true);
          //            $(`#inputEducationEndDate--${index}`).parent().find('label').text("Estimasi Kelulusan")
          //        } else {
          //            enrollElement.prop("checked", false);
          //            $(`#inputEducationEndDate--${index}`).parent().find('label').text("Berakhir pada")
          //        }
          if (jsonEducationData[i].endDate == null && jsonEducationData[i].startDate != null) {
               enrollElement.prop("checked", true);
               enrollElement.trigger("change");
          } else {
               $(`#inputEducationEndDate--${index}`).val(jsonEducationData[i].endDate).trigger("input");
               enrollElement.prop("checked", false);
               enrollElement.trigger("change");
          }
     }
};

const saveSkillData = () => {
     let skillData = [...document.querySelectorAll("[id^=inputSkillName--]")].map((input) => {
          return {
               id: $(`#inputSkillId--${input.id.split("--")[1]}`).val(),
               name: input.value,
               level: $(`#selectSkillLevel--${input.id.split("--")[1]}`).val(),
               category: $(`#selectSkillCategory--${input.id.split("--")[1]}`).val(),
          };
     });

     let newSkillData = skillData.filter((el) => {
          return el.name !== "";
     });

     sessionStorage.setItem("skillData", JSON.stringify(newSkillData));
};

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

     const hardSkill = jsonSkillData.filter((row) => row.category == 1);
     const softSkill = jsonSkillData.filter((row) => row.category == 2);

     countAllSkill = jsonSkillData.length;
     countHardSkill = hardSkill.length;
     let index = 0;

     for (let i = 0; i < hardSkill.length; i++) {
          $("#btnAddSectionSkill").click();

          index++;
          $(`#inputSkillId--${index}`).val(hardSkill[i].id);
          $(`#inputSkillName--${index}`).val(hardSkill[i].name).trigger("keyup");
          $(`#selectSkillLevel--${index}`).val(hardSkill[i].level).trigger("change");
          $(`#selectSkillCategory--${index}`).val(hardSkill[i].category).trigger("change");
     }

     countSoftSkill = softSkill.length;

     for (let i = 0; i < softSkill.length; i++) {
          $("#btnAddSectionSkill").click();

          index++;
          $(`#inputSkillId--${index}`).val(softSkill[i].id);
          $(`#inputSkillName--${index}`).val(softSkill[i].name).trigger("keyup");
          $(`#selectSkillLevel--${index}`).val(softSkill[i].level).trigger("change");
          $(`#selectSkillCategory--${index}`).val(softSkill[i].category).trigger("change");
     }
};

const saveWorkData = () => {
     let workData = [...document.querySelectorAll("[id^=inputCompanyName--]")].map((input) => {
          let endDate;
          if ($(`#workUnEnroll--${input.id.split("--")[1]}`).is(":checked")) {
               endDate = null;
          } else {
               endDate = $(`#inputWorkEndDate--${input.id.split("--")[1]}`).val();
          }
          return {
               id: $(`#inputWorkId--${input.id.split("--")[1]}`).val(),
               companyName: input.value,
               position: $(`#inputPosition--${input.id.split("--")[1]}`).val(),
               status: $(`#selectWorkStatus--${input.id.split("--")[1]}`).val(),
               startDate: $(`#inputWorkStartDate--${input.id.split("--")[1]}`).val(),
               endDate: endDate,
               jobDescription: $(`#inputJobDescription--${input.id.split("--")[1]}`).summernote("isEmpty") ? null : $(`#inputJobDescription--${input.id.split("--")[1]}`).summernote("code"),
               projectSpecification: $(`#inputProjectSpesification--${input.id.split("--")[1]}`).summernote("isEmpty") ? null : $(`#inputProjectSpesification--${input.id.split("--")[1]}`).summernote("code"),
          };
     });

     let newWorkData = workData.filter((el) => {
          return el.companyName !== "";
     });

     sessionStorage.setItem("workData", JSON.stringify(newWorkData));
};

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
          $(`#inputWorkId--${index}`).val(jsonWorkData[i].id);
          $(`#inputCompanyName--${index}`).val(jsonWorkData[i].companyName).trigger("input");
          $(`#inputPosition--${index}`).val(jsonWorkData[i].position).trigger("keyup");
          $(`#inputWorkStartDate--${index}`).val(jsonWorkData[i].startDate).trigger("input");
          $(`#selectWorkStatus--${index}`).val(jsonWorkData[i].status).trigger("change");
          $(`#inputJobDescription--${index}`).summernote("code", !jsonWorkData[i].jobDescription.length ? "<ul><li></li></ul>" : jsonWorkData[i].jobDescription);
          $(`#inputProjectSpesification--${index}`).summernote("code", !jsonWorkData[i].projectSpecification.length ? "<ul><li></li></ul>" : jsonWorkData[i].projectSpecification);

          let enrollElement = $(`#workUnEnroll--${index}`);
          if (jsonWorkData[i].endDate == null && jsonWorkData[i].startDate != null) {
               enrollElement.prop("checked", true);
               enrollElement.trigger("change");
          } else {
               $(`#inputWorkEndDate--${index}`).val(jsonWorkData[i].endDate).trigger("input");
               enrollElement.prop("checked", false);
               enrollElement.trigger("change");
          }
     }
};

const saveProjectData = () => {
     let projectData = [...document.querySelectorAll("[id^=inputProjectName--]")].map((input) => {
          let startDate, endDate;
          if ($(`#projectUnEnroll--${input.id.split("--")[1]}`).is(":checked")) {
               startDate = null;
               endDate = null;
          } else {
               endDate = $(`#inputProjectEndDate--${input.id.split("--")[1]}`).val();
          }
          startDate = $(`#inputProjectStartDate--${input.id.split("--")[1]}`).val();

          const skill = $(`#inputProjectSkill--${input.id.split("--")[1]}`).val();
          const site = $(`#inputProjectSite--${input.id.split("--")[1]}`).val(); // Added this line

          return {
               id: $(`#inputProjectId--${input.id.split("--")[1]}`).val(),
               name: input.value,
               description: $(`#inputProjectJobDescription--${input.id.split("--")[1]}`).summernote("isEmpty") ? null : $(`#inputProjectJobDescription--${input.id.split("--")[1]}`).summernote("code"),
               site: site, // Added this line
               startDate: $(`#inputProjectStartDate--${input.id.split("--")[1]}`).val(),
               endDate: $(`#inputProjectEndDate--${input.id.split("--")[1]}`).val(),
               skill: skill,
          };
     });

     let newProjectData = projectData.filter((el) => {
          return el.name !== "";
     });

     sessionStorage.setItem("projectData", JSON.stringify(newProjectData));
};

const setProjectData = (jsonProjectData) => {
     //    jsonProjectData.sort((a, b) => new Date(b.startDate) - new Date(a.startDate));
     $("#sectionProject").fadeOut().empty().fadeIn(500);
     $("#txtProjectFrame").fadeOut().empty().fadeIn(500);
     $("#frame").contents().find("#txtProjectFrame").fadeOut().empty().fadeIn();

     countSectionProject = 0;
     for (let i = 0; i < jsonProjectData.length; i++) {
          $("#btnAddSectionProject").click();

          let index = i + 1;
          $(`#inputProjectId--${index}`).val(jsonProjectData[i].id);
          $(`#inputProjectName--${index}`).val(jsonProjectData[i].projectName).trigger("input");
          $(`#inputProjectSite--${index}`).val(jsonProjectData[i].site).trigger("input");
          $(`#inputProjectJobDescription--${index}`).summernote("code", !jsonProjectData[i].description.length ? "<ul><li></li></ul>" : jsonProjectData[i].description);
          $(`#inputProjectStartDate--${index}`).val(jsonProjectData[i].startDate).trigger("input");
          //        $(`#inputProjectEndDate--${index}`).val(jsonProjectData[i].endDate).trigger('input');
          $(`#inputProjectSkill--${index}`).val(jsonProjectData[i].skill).trigger("input");

          let enrollElement = $(`#projectUnEnroll--${index}`);
          if (jsonProjectData[i].endDate == null && jsonProjectData[i].startDate != null) {
               enrollElement.prop("checked", true);
               enrollElement.trigger("change");
          } else {
               $(`#inputProjectEndDate--${index}`).val(jsonProjectData[i].endDate).trigger("input");
               enrollElement.prop("checked", false);
               enrollElement.trigger("change");
          }
     }
};

const saveTrainingData = () => {
     let trainingData = [...document.querySelectorAll("[id^=inputTrainingName--]")].map((input) => {
          return {
               id: $(`#inputTrainingId--${input.id.split("--")[1]}`).val(),
               name: input.value,
               date: $(`#inputTrainingDate--${input.id.split("--")[1]}`).val(),
               syllabus: $(`#inputTrainingSyllabus--${input.id.split("--")[1]}`).val(),
          };
     });

     let newTrainingData = trainingData.filter((el) => {
          return el.name !== "";
     });

     sessionStorage.setItem("trainingData", JSON.stringify(newTrainingData));
};

const setTrainingData = (jsonTrainingData) => {
     $("#sectionTraining").fadeOut().empty().fadeIn(500);
     $("#txtTrainingFrame").fadeOut().empty().fadeIn(500);
     $("#frame").contents().find("#txtTrainingFrame").fadeOut().empty().fadeIn();
     countSectionTraining = 0;
     for (let i = 0; i < jsonTrainingData.length; i++) {
          $("#btnAddSectionTraining").click();

          let index = i + 1;
          $(`#inputTrainingId--${index}`).val(jsonTrainingData[i].id);
          $(`#inputTrainingName--${index}`).val(jsonTrainingData[i].trainingName).trigger("input");
          $(`#inputTrainingDate--${index}`).val(jsonTrainingData[i].date).trigger("input");
          $(`#inputTrainingSyllabus--${index}`).val(jsonTrainingData[i].syllabus).trigger("input");
     }
};

const saveCertificationData = () => {
     let certificationData = [...document.querySelectorAll("[id^=inputCertificationName--]")].map((input) => {
          let validityPeriod, releaseDate;
          if ($(`#certificationUnEnroll--${input.id.split("--")[1]}`).is(":checked")) {
               validityPeriod = null;
               releaseDate = null;
          } else {
               releaseDate = $(`#inputCertificationReleaseDate--${input.id.split("--")[1]}`).val();
               validityPeriod = $(`#inputCertificationValidityPeriod--${input.id.split("--")[1]}`).val();
          }

          const institutionName = $(`#inputCertificationInstitute--${input.id.split("--")[1]}`).val();
          const institution = [
               {
                    name: institutionName,
               },
          ];
          return {
               id: $(`#inputCertificationId--${input.id.split("--")[1]}`).val(),
               name: input.value,
               institutes: institution,
               releaseDate: $(`#inputCertificationReleaseDate--${input.id.split("--")[1]}`).val(),
               validityPeriod: $(`#inputCertificationValidityPeriod--${input.id.split("--")[1]}`).val(),
          };
     });

     let newCertificationData = certificationData.filter((el) => {
          return el.name !== "";
     });

     sessionStorage.setItem("certificationData", JSON.stringify(newCertificationData));
};

const setCertificationData = (jsonCertificationData) => {
     $("#sectionCertification").fadeOut().empty().fadeIn(500);
     $("#txtCertificationFrame").fadeOut().empty().fadeIn(500);
     $("#frame").contents().find("#txtCertificationFrame").fadeOut().empty().fadeIn();

     countSectionCertification = 0;
     for (let i = 0; i < jsonCertificationData.length; i++) {
          $("#btnAddSectionCertification").click();

          let index = i + 1;
          $(`#inputCertificationId--${index}`).val(jsonCertificationData[i].id);
          $(`#inputCertificationName--${index}`).val(jsonCertificationData[i].certificationName).trigger("input");
          $(`#inputCertificationInstitute--${index}`).val(jsonCertificationData[i].institution[0].name).trigger("input");
          $(`#inputCertificationReleaseDate--${index}`).val(jsonCertificationData[i].releaseDate).trigger("input");
          $(`#inputCertificationValidityPeriod--${index}`).val(jsonCertificationData[i].validityPeriod).trigger("input");

          let enrollElement = $(`#certificationUnEnroll--${index}`);
          if (jsonCertificationData[i].validityPeriod == null && jsonCertificationData[i].releaseDate != null) {
               enrollElement.prop("checked", true);
               enrollElement.trigger("change");
          } else {
               enrollElement.prop("checked", false);
               enrollElement.trigger("change");
          }
     }
};

const saveOrganizationData = () => {
     let organizationData = [...document.querySelectorAll("[id^=inputOrganizationName--]")].map((input) => {
          let endDate;
          if ($(`#organizationUnEnroll--${input.id.split("--")[1]}`).is(":checked")) {
               endDate = null;
          } else {
               endDate = $(`#inputOrganizationEndDate--${input.id.split("--")[1]}`).val();
          }
          return {
               id: $(`#inputOrganizationId--${input.id.split("--")[1]}`).val(),
               organizationName: input.value,
               position: $(`#inputOrganizationPosition--${input.id.split("--")[1]}`).val(),
               startDate: $(`#inputOrganizationStartDate--${input.id.split("--")[1]}`).val(),
               endDate: endDate,
          };
     });

     let newOrganizationData = organizationData.filter((el) => {
          return el.organizationName !== "";
     });

     sessionStorage.setItem("organizationData", JSON.stringify(newOrganizationData));
};

const setOrganizationData = (jsonOrganizationData) => {
     $("#sectionOrganization").fadeOut().empty().fadeIn(500);
     $("#txtOrganizationFrame").fadeOut().empty().fadeIn(500);
     $("#frame").contents().find("#txtOrganizationFrame").fadeOut().empty().fadeIn();
     countSectionOrganization = 0;
     for (let i = 0; i < jsonOrganizationData.length; i++) {
          $("#btnAddSectionOrganization").click();

          let index = i + 1;
          $(`#inputOrganizationId--${index}`).val(jsonOrganizationData[i].id);
          $(`#inputOrganizationName--${index}`).val(jsonOrganizationData[i].organizationName).trigger("input");
          $(`#inputOrganizationPosition--${index}`).val(jsonOrganizationData[i].position).trigger("input");
          $(`#inputOrganizationStartDate--${index}`).val(jsonOrganizationData[i].startDate).trigger("input");
          let enrollElement = $(`#organizationUnEnroll--${index}`);
          if (jsonOrganizationData[i].endDate == null && jsonOrganizationData[i].startDate != null) {
               enrollElement.prop("checked", true);
               enrollElement.trigger("change");
          } else {
               $(`#inputOrganizationEndDate--${index}`).val(jsonOrganizationData[i].endDate).trigger("input");
               enrollElement.prop("checked", false);
               enrollElement.trigger("change");
          }
     }
};

const saveExperienceData = () => {
     let experienceData = [...document.querySelectorAll("[id^=inputExperienceName--]")].map((input) => {
          return {
               id: $(`#inputExperienceId--${input.id.split("--")[1]}`).val(),
               experienceName: input.value,
               institute: $(`#inputExperienceInstitute--${input.id.split("--")[1]}`).val(),
               date: $(`#inputExperienceDate--${input.id.split("--")[1]}`).val(),
               position: $(`#inputPositionName--${input.id.split("--")[1]}`).val(),
               description: $(`#inputDescriptionName--${input.id.split("--")[1]}`).val(),
          };
     });

     let newExperienceData = experienceData.filter((el) => {
          return el.experienceName !== "";
     });

     sessionStorage.setItem("experienceData", JSON.stringify(newExperienceData));
};

const setExperienceData = (jsonExperienceData) => {
     $("#sectionExperience").fadeOut().empty().fadeIn(500);
     $("#txtExperienceFrame").fadeOut().empty().fadeIn(500);
     $("#frame").contents().find("#txtExperienceFrame").fadeOut().empty().fadeIn();
     countSectionExperience = 0;
     for (let i = 0; i < jsonExperienceData.length; i++) {
          $("#btnAddSectionExperience").click();

          let index = i + 1;
          $(`#inputExperienceId--${index}`).val(jsonExperienceData[i].id);
          $(`#inputExperienceName--${index}`).val(jsonExperienceData[i].experienceName).trigger("input");
          $(`#inputExperienceInstitute--${index}`).val(jsonExperienceData[i].institute).trigger("input");
          $(`#inputPositionName--${index}`).val(jsonExperienceData[i].position).trigger("input");
          $(`#inputDescriptionName--${index}`).val(jsonExperienceData[i].description).trigger("input");
          $(`#inputExperienceDate--${index}`).val(jsonExperienceData[i].date).trigger("input");
     }
};

const saveAwardData = () => {
     let awardData = [...document.querySelectorAll("[id^=inputAwardName--]")].map((input) => {
          return {
               id: $(`#inputAwardId--${input.id.split("--")[1]}`).val(),
               name: input.value,
               institute: $(`#inputAwardInstitute--${input.id.split("--")[1]}`).val(),
               year: $(`#inputAwardYear--${input.id.split("--")[1]}`).val(),
          };
     });

     let newAwardData = awardData.filter((el) => {
          return el.name !== "";
     });

     sessionStorage.setItem("awardData", JSON.stringify(newAwardData));
};

const setAwardData = (jsonAwardData) => {
     $("#sectionAward").empty().fadeIn(500);
     $("#txtAwardFrame").empty().fadeIn(500);
     $("#frame").contents().find("#txtAwardFrame").fadeOut().empty().fadeIn();
     countSectionAward = 0;
     for (let i = 0; i < jsonAwardData.length; i++) {
          $("#btnAddSectionAward").click();

          let index = i + 1;
          $(`#inputAwardId--${index}`).val(jsonAwardData[i].id);
          $(`#inputAwardName--${index}`).val(jsonAwardData[i].awardName).trigger("keyup");
          $(`#inputAwardInstitute--${index}`).val(jsonAwardData[i].institute).trigger("keyup");
          $(`#inputAwardYear--${index}`).val(jsonAwardData[i].year).trigger("input");
     }
};

const setDefaultBtnSave = () => {
     $("#btnSave").prop("disabled", false).html('<i class="fa fa-save"></i>  Simpan Perubahan');
};
