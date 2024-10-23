let countSectionLanguage = 0;
//let languageLevelLayout = "<option value=''>-- Pilih --</option>";

// fetch("/api/get-language-levels")
//   .then((response) => response.json())
//   .then((languageLevels) => {
//     languageLevels.forEach((level) => {
//       languageLevelLayout += `<option value='${level}'>${level}</option>`;
//     });
//   })
//   .catch((error) => {
//     console.error("Error fetching language levels:", error);
//   });

$("#btnAddSectionLanguage").click(function ($e) {
  $e.preventDefault();
  countSectionLanguage++;
  $("#sectionLanguage").append(`
    <div id="sectionLanguageForm--${countSectionLanguage}" class="border rounded py-3 col-sm-11 mt-3 section-list">
      <div id="sectionLanguageCollapse--${countSectionLanguage}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseLanguage--${countSectionLanguage}" role="button" aria-expanded="false"
          aria-controls="collapseLanguage--${countSectionLanguage}">
          <div class="col-md-11 col-xs-11">
            <span class="bullet"></span>
            <b id="txtLanguage--${countSectionLanguage}"></b>
            <span id="txtLanguageLevel-x-${countSectionLanguage}"></span>
            <b id="txtLanguageDefault--${countSectionLanguage}">(Anda belum mengisi)</b>
            
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
      </div>
      <div class="collapse container" id="collapseLanguage--${countSectionLanguage}">
        <div class="mt-4"></div>
        <div class="row">
          <div class="form-group col-md-12">
            
            <label>Bahasa </label>
            <select id="selectLanguageId--${countSectionLanguage}" data-collapse="#collapseLanguage--${countSectionLanguage}" class="form-control required selectLanguageLevel--${countSectionLanguage}--2" onchange="
                      if($(this).val() === ''){
                          $('#txtLanguageLevel--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      } else {
                          $('#txtLanguageLevel--${countSectionLanguage}').text('('+$(this).find('option:selected').text()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      }
                          isLanguageValueBlank(); autoSave(); validateRequire($(this))">
                          <option value="" disabled selected>-- Pilih --</option>
            </select>
          </div>
        </div>
        <div class="row">
          <div class="form-group col-md-6">
              <label>Kemampuan</label>
              <input type="text" class="form-control" value="Writing" id="selectLanguageCompetence--1" disabled>
          </div>
          <div class="form-group col-md-6">
                <label>Tingkat</label>
                <select data-collapse="#collapseLanguage--${countSectionLanguage}" class="form-control required selectLanguageLevel--${countSectionLanguage}--2" onchange="
                      if($(this).val() === ''){
                          $('#txtLanguageLevel--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      } else {
                          $('#txtLanguageLevel--${countSectionLanguage}').text('('+$(this).find('option:selected').text()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      }
                          isLanguageValueBlank(); autoSave(); validateRequire($(this))">
                          <option value="" disabled selected>-- Pilih --</option>
                          <option value="BEGINNER">Beginner</option>
                          <option value="INTERMEDIATE">Intermediate</option>
                          <option value="ADVANCED">Advanced</option>
                </select>
          </div>
          <div class="form-group col-md-6">
              <label>Kemampuan</label>
              <input type="text" class="form-control" value="Reading" id="selectLanguageCompetence--2" disabled>
          </div>
          <div class="form-group col-md-6">
                <label>Tingkat</label>
                <select data-collapse="#collapseLanguage--${countSectionLanguage}" class="form-control required selectLanguageLevel--${countSectionLanguage}--2" onchange="
                      if($(this).val() === ''){
                          $('#txtLanguageLevel--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      } else {
                          $('#txtLanguageLevel--${countSectionLanguage}').text('('+$(this).find('option:selected').text()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      }
                      isLanguageValueBlank(); autoSave(); validateRequire($(this))">
                      <option value="" disabled selected>-- Pilih --</option>
                      <option value="BEGINNER">Beginner</option>
                      <option value="INTERMEDIATE">Intermediate</option>
                      <option value="ADVANCED">Advanced</option>
                </select>
          </div>
          <div class="form-group col-md-6">
              <label>Kemampuan</label>
              <input type="text" class="form-control" value="Speaking" id="selectLanguageCompetence--3" disabled>
          </div>
          <div class="form-group col-md-6">
                <label>Tingkat</label>
                <select data-collapse="#collapseLanguage--${countSectionLanguage}" class="form-control required selectLanguageLevel--${countSectionLanguage}--2" onchange="
                      if($(this).val() === ''){
                          $('#txtLanguageLevel--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      } else {
                          $('#txtLanguageLevel--${countSectionLanguage}').text('('+$(this).find('option:selected').text()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#frame').contents().find('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                          $('#txtListLanguageLevel1--${countSectionLanguage}').text('Reading &emsp;&nbsp;&nbsp;: '+$(this).find('option:selected').text()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      }
                          isLanguageValueBlank(); autoSave(); validateRequire($(this))">
                          <option value="" disabled selected>-- Pilih --</option>
                          <option value="BEGINNER">Beginner</option>
                          <option value="INTERMEDIATE">Intermediate</option>
                          <option value="ADVANCED">Advanced</option>
                </select>
          </div>
        </div>
      </div>
    </div>
    <div id="sectionLanguageBtnDelete--${countSectionLanguage}" class="row col-sm-1 mt-4">
      <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
        onclick="deleteSectionLanguage(${countSectionLanguage});">
        <i class="text-bottom fa fa-trash"></i>
      </a>
    </div>
    `);

  // Panggil displayData untuk elemen select yang baru
  displayData(
    "/api/references/get-by-group_1/bahasa",
    `selectLanguageId--${countSectionLanguage}`
  );

  $(`#sectionLanguageForm--${countSectionLanguage}`).hide().fadeIn(500);
});
// Frame di Live Preview sebelah kanan (sebelum diisi)
$("#frame").contents().find("#txtLanguageProfiency").append(`
<tr id="txtListLanguageProfiency--${countSectionLanguage}">
  <td>
    <span class="bullet"></span>
  </td>
  <td>
    <div id="listLanguage--${countSectionLanguage}">
        <b id="txtListLanguageName--${countSectionLanguage}"></b><br/>
        </span><span id="txtListLanguageLevel--${countSectionLanguage}"></span><br/>
        </span><span id="txtListLanguageLevel1--${countSectionLanguage}"></span><br/>
        </span><span id="txtListLanguageLevel2--${countSectionLanguage}"></span><br/>
    </div>
    <b id="txtLanguageDefaultFrame--${countSectionLanguage}">__________________________________________________</b>
    <br>
  </td>
</tr>`);
$("#frame")
  .contents()
  .find(`#txtListLanguageProfiency--${countSectionLanguage}`)
  .fadeTo(100, 0.3, function () {
    $(this).fadeTo(500, 1.0);
  });

// Frame di dalam tombol 'Preview CV' (sebelum diisi)
$("#txtLanguageProfiency").append(`
<tr id="txtListLanguageProfiency--${countSectionLanguage}">
  <td>
      <span class="bullet"></span>
  </td>
  <td>
    <b id="txtListLanguageName--${countSectionLanguage}"></b><br/>
    </span><span id="txtListLanguageLevel--${countSectionLanguage}"></span><br/>
    </span><span id="txtListLanguageLevel1--${countSectionLanguage}"></span><br/>
    </span><span id="txtListLanguageLevel2--${countSectionLanguage}"></span><br/>
    <b id="txtLanguageDefaultFrame--${countSectionLanguage}">
      __________________________________________________
    </b>
    <br>
  </td>
</tr>
`);

$(`#txtListLanguageProfiency--${countSectionLanguage}`).fadeTo(
  100,
  0.3,
  function () {
    $(this).fadeTo(500, 1.0);
  }
);

for (let i = 0; i < countSectionLanguage; i++) {
  let index = i + 1;

  if ($(`#collapseLanguage--${index}`).hasClass("collapse show")) {
    $(`#sectionLanguageCollapse--${index}`).click();
  }
}

isLanguageValueBlank();
renderAutocomplete();

function deleteSectionLanguage(id) {
  // const idToDelete = $(`#inputLanguageId--${id}`).val();
  // console.warn("ID TO DELETE : "+idToDelete)
  let idUserLanguages = [];

  fetch("/api/get-user-language")
    .then((response) => response.json())
    .then((languageLevels) => {
      const uniqueLanguages = new Set();
      const uniqueLanguagesTwo = new Set();
      languageLevels.forEach((idUserLanguage) => {
        uniqueLanguages.add(idUserLanguage.languageId);
        uniqueLanguagesTwo.add(idUserLanguage.languageName);
      });
      idUserLanguages = Array.from(uniqueLanguages);
      nameUserLanguages = Array.from(uniqueLanguagesTwo);
    })
    .catch((error) => {
      console.error("Error fetching language userLanguages:", error);
    });

  iziToast.question({
    timeout: 20000,
    close: false,
    overlay: true,
    displayMode: "once",
    id: "question",
    zindex: 999,
    message: "Yakin ingin menghapus data?",
    position: "center",
    buttons: [
      [
        "<button><b>Ya</b></button>",
        function (instance, toast) {
          // Delete data from the API
          deleteData(
            "/api/delete-user-language/" +
              idUserLanguages[id - 1] +
              "?hhUserId=" +
              hhUserId
          )
            .then((r) => console.log(r))
            .catch((r) => console.log(r));
          instance.hide(
            {
              transitionOut: "fadeOut",
            },
            toast,
            "button"
          );
          $(`#sectionLanguageForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionLanguageBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtListLanguageProfiency--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
        },
        true,
      ],
      [
        "<button>Batal</button>",
        function (instance, toast) {
          instance.hide(
            {
              transitionOut: "fadeOut",
            },
            toast,
            "button"
          );
        },
      ],
    ],
  });
}

function isLanguageValueBlank() {
  for (var i = 0; i < countSectionLanguage; i++) {
    var index = i + 1;
    if (
      $(`#inputLanguageName--${index}`).val() !== "" ||
      $(`.selectLanguageLevel--${index}--1`).val() !== "" ||
      $(`.selectLanguageLevel--${index}--2`).val() !== "" ||
      $(`.selectLanguageLevel--${index}--3`).val() !== ""
    ) {
      $(`#txtLanguageDefault--${index}`).hide();
      $("#frame").contents().find(`#txtLanguageDefaultFrame--${index}`).hide();
      $("#frame").contents().find(`#listLanguage--${index}`).show();
      $(`#txtLanguageDefaultFrame--${index}`).hide();
    } else {
      $(`#txtLanguageDefault--${index}`).show();
      $("#frame").contents().find(`#txtLanguageDefaultFrame--${index}`).show();
      $("#frame").contents().find(`#listLanguage--${index}`).hide();
      $(`#txtLanguageDefaultFrame--${index}`).show();
    }
  }
}

// Function to update the displayed language text
function updateLanguageDisplay(count) {
  const languageInput = $(`#inputLanguageName--${count}`);
  const txtLanguageDefault = $(`#txtLanguageDefault--${count}`);
  const txtLanguage = $(`#txtLanguage--${count}`);

  if (languageInput.val() !== "") {
    txtLanguageDefault.text(languageInput.val()).fadeTo(100, 0.3, function () {
      $(this).fadeTo(100, 1.0);
    });
  } else {
    txtLanguageDefault
      .text("(Anda belum mengisi)")
      .fadeTo(100, 0.3, function () {
        $(this).fadeTo(100, 1.0);
      });
  }
}

function displayData(url, selectId) {
  $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    success: function (response) {
      var data = response;
      var select = $("#" + selectId);

      select.find('option:not([value=""])').remove();
      // select.empty().append('<option value="" disabled selected>-- Pilih --</option>');

      data.forEach(function (reference) {
        var option = $("<option></option>")
          .attr("value", reference.id)
          .text(reference.reference_name);
        select.append(option);
      });
    },
    error: function (error) {
      console.error("Error fetching data:", error);
    },
  });
}
