let majorData;
let universityData;
let nationalityData;
let languageData;
let jobData;
let skillData;

$(window).on("load", function () {
     autoCompleteBirtplace();
     autoCompleteProvince();
     console.log("Initialize Autocomplete..");
});

const createAutocomplete = (selector, data) => {
     let autoCompleteData = $.map(data, (value, key) => {
          return {
               label: value.name,
               value: value.name,
          };
     });

     $("body")
          .find(selector)
          .autocomplete({
               source: (request, response) => {
                    let results;
                    results = $.ui.autocomplete.filter(autoCompleteData, request.term);
                    response(results);
               },
               select: function (event, ui) {
                    $(`#${this.id}`).val(ui.item.value).trigger("keyup");
                    autoSave();
               },
          });
};

const renderAutocomplete = () => {
     console.log("Render Autocomplete..");
     createAutocomplete("._autocomplete-major", majorData);
     createAutocomplete("._autocomplete-nationality", nationalityData);
     createAutocomplete("._autocomplete-university", universityData);
     createAutocomplete("._autocomplete-language", languageData);
     createAutocomplete("._autocomplete-job", jobData);
     createAutocomplete("._autocomplete-skill", skillData);
};

const runAutocompleteInitialize = async () => {
     if (readCookie("refreshToken") != null && readCookie("refreshToken") != "") {
          await getData(baseUrl + "/api/get-major").then((response) => {
               majorData = response;
          });

          await getData(baseUrl + "/api/get-job").then((response) => {
               jobData = response.filter(function (job) {
                    return job.name !== "Belum Bekerja";
               });
          });

          await getData(baseUrl + "/api/get-university").then((response) => {
               universityData = response;
          });
     }
     await getData(baseUrl + "/api/get-all-language").then((response) => {
          languageData = response;
     });

     await getData(baseUrl + "/api/get-nationality").then((response) => {
          nationalityData = response;
     });

     await getData(baseUrl + "/api/get-skill").then((response) => {
          skillData = response;
     });
};

function autoCompleteBirtplace() {
     $.ajax({
          url: baseUrl + "/assets/json/birthplace.json",
          type: "GET",
          contentType: "application/json; charset=utf-8",
          success: function (data) {
               $("#inputBirthPlace").autocomplete({
                    source: data,
                    select: function (event, ui) {
                         $(`#${this.id}`).val(ui.item.value).trigger("keyup");
                         autoSave();
                    },
               });
               $("#inputCity").autocomplete({
                    source: data,
                    select: function (event, ui) {
                         $(`#${this.id}`).val(ui.item.value).trigger("keyup");
                         autoSave();
                    },
               });
          },
          error: function (errormessage) {
               console.log(errormessage);
          },
     });
}

function autoCompleteProvince() {
     $.ajax({
          url: baseUrl + "/assets/json/province.json",
          type: "GET",
          contentType: "application/json; charset=utf-8",
          success: function (data) {
               $("#inputProvince").autocomplete({
                    source: data,
                    select: function (event, ui) {
                         $(`#${this.id}`).val(ui.item.value).trigger("keyup");
                         autoSave();
                    },
               });
          },
          error: function (errormessage) {
               console.log(errormessage);
          },
     });
}
