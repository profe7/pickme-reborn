let countSectionSkill = 0;

$("#btnAddSectionSkill").click(function ($e) {
  $e.preventDefault();
  countSectionSkill++;
  $("#sectionSkill").append(`
      <div id="sectionSkillForm--${countSectionSkill}" class="border rounded py-3 col-sm-11 mt-3 section-list">
        <div id="sectionSkillCollapse--${countSectionSkill}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseSkill--${countSectionSkill}" role="button" aria-expanded="false"
          aria-controls="collapseSkill--${countSectionSkill}">
          <div class="col-md-11 col-xs-11">
            <span class="bullet"></span>
            <b id="txtSkillPosition--${countSectionSkill}"></b>
            <b id="txtSkillName--${countSectionSkill}"></b>
            <span id="txtSkillStartDate--${countSectionSkill}"></span>
            <span id="txtSkillEndDate--${countSectionSkill}"></span>
            <b id="txtSkillDefault--${countSectionSkill}">(Anda belum mengisi)</b>
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
        </div>
        <div class="collapse container" id="collapseSkill--${countSectionSkill}">
          <div class="mt-4"></div>
          <div class="row">
            <div class="form-group col-md-12">
              <label>Skill</label>
              <select id="inputSkillName--${countSectionSkill}" class="form-control required" onchange="
                  if($(this).val() === ''){
                      $('#txtSkillName--${countSectionSkill}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#frame').contents().find('#txtSkillNameFrame--${countSectionSkill}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#txtSkillNameFrame--${countSectionSkill}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  } else {
                      $('#txtSkillName--${countSectionSkill}').text(' at '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#frame').contents().find('#txtSkillNameFrame--${countSectionSkill}').text(' at '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#txtSkillNameFrame--${countSectionSkill}').text(' at '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  }
                  isSkillValueBlank(); autoSave(); validateRequire($(this))" required="">
                  <option value="" disabled selected>-- Pilih --</option>
              </select>
            </div>
            <div class="form-group col-md-6">
  
      </div>

      </div>
          <div class="row">
            <div class="form-group form-check ml-3">
                
            </div>
          </div>
          <div class="row">
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
              <label>Kategori</label>
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
                    <option value="SOFT_SKILL">Soft-skill</option>
                    <option value="HARD_SKILL">Hard-skill</option>
              </select>
            </div>
          </div>
        </div>
      </div>
      <div id="sectionSkillBtnDelete--${countSectionSkill}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deletesectionSkill(${countSectionSkill});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>
    `);

  displayData(
    "/api/references/get-by-group_1/skills",
    `inputSkillName--${countSectionSkill}`
  );

  $(`#sectionSkillForm--${countSectionSkill}`).hide().fadeIn(500);

  $("#frame").contents().find("#txtSkillFrame").append(`
        <tr id="txtListSkill--${countSectionSkill}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtSkillPositionFrame--${countSectionSkill}"></b>
            <b id="txtSkillNameFrame--${countSectionSkill}"></b>
            <span id="txtSkillStartDateFrame--${countSectionSkill}"></span>
            <span id="txtSkillEndDateFrame--${countSectionSkill}"></span>
            <b id="txtSkillDefaultFrame--${countSectionSkill}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $("#frame")
    .contents()
    .find(`#txtListSkill--${countSectionSkill}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  $("#txtSkillFrame").append(`
        <tr id="txtListSkill--${countSectionSkill}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtSkillPositionFrame--${countSectionSkill}"></b>
            <b id="txtSkillNameFrame--${countSectionSkill}"></b>
            <span id="txtSkillStartDateFrame--${countSectionSkill}"></span>
            <span id="txtSkillEndDateFrame--${countSectionSkill}"></span>
            <b id="txtSkillDefaultFrame--${countSectionSkill}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $(`#txtListSkill--${countSectionSkill}`).fadeTo(100, 0.3, function () {
    $(this).fadeTo(500, 1.0);
  });

  for (let i = 0; i < countSectionSkill; i++) {
    let index = i + 1;

    if ($(`#collapseSkill--${index}`).hasClass("collapse show")) {
      $(`#sectionSkillCollapse--${index}`).click();
    }
  }

  isSkillValueBlank();

  renderAutocomplete();
});

function deletesectionSkill(id) {
  // countSectionSkill--;
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
          $(`#sectionSkillForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionSkillBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtListSkill--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");

          deleteData(
            "/api/delete-user-Skill/" + $(`#inputSkillId--${id}`).val()
          )
            .then((r) => console.log(r))
            .catch((r) => console.log(r));
        },
        true,
      ],
      [
        "<button>Batal</button>",
        function (instance, toast) {
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");
        },
      ],
    ],
  });
}

function isSkillValueBlank() {
  for (var i = 0; i < countSectionSkill; i++) {
    var index = i + 1;
    if (
      $(`#inputSkillName--${index}`).val() !== "" ||
      $(`#inputSkillStartDate--${index}`).val() !== "" ||
      $(`#inputSkillEndDate--${index}`).val() !== ""
    ) {
      $(`#txtSkillDefault--${index}`).hide();
      $("#frame").contents().find(`#txtSkillDefaultFrame--${index}`).hide();
      $(`#txtSkillDefaultFrame--${index}`).hide();
    } else {
      $(`#txtSkillDefault--${index}`).show();
      $("#frame").contents().find(`#txtSkillDefaultFrame--${index}`).show();
      $(`#txtSkillDefaultFrame--${index}`).show();
    }
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
