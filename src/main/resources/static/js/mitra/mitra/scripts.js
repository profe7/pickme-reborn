"use strict";

// DropzoneJS
if (window.Dropzone) {
  Dropzone.autoDiscover = false;
}

// Basic confirm box
$("[data-confirm]").each(function () {
  var me = $(this),
    me_data = me.data("confirm");

  me_data = me_data.split("|");
  me.fireModal({
    title: me_data[0],
    body: me_data[1],
    buttons: [
      {
        text: me.data("confirm-text-yes") || "Yes",
        class: "btn btn-danger btn-shadow",
        handler: function () {
          eval(me.data("confirm-yes"));
        },
      },
      {
        text: me.data("confirm-text-cancel") || "Cancel",
        class: "btn btn-secondary",
        handler: function (modal) {
          $.destroyModal(modal);
          eval(me.data("confirm-no"));
        },
      },
    ],
  });
});

// Global
$(function () {
  $("[data-toggle='search']").click(function () {
    var body = $("body");

    if (body.hasClass("search-gone")) {
      body.addClass("search-gone");
      body.removeClass("search-show");
    } else {
      body.removeClass("search-gone");
      body.addClass("search-show");
    }
  });

  // tooltip
  $("[data-toggle='tooltip']").tooltip();

  // popover
  $('[data-toggle="popover"]').popover({
    container: "body",
  });

  // Select2
  if (jQuery().select2) {
    $(".select2").select2();
  }

  // Selectric
  if (jQuery().selectric) {
    $(".selectric").selectric({
      disableOnMobile: false,
      nativeOnMobile: false,
    });
  }

  $(".notification-toggle").dropdown();
  $(".notification-toggle")
    .parent()
    .on("shown.bs.dropdown", function () {
      $(".dropdown-list-icons").niceScroll({
        cursoropacitymin: 0.3,
        cursoropacitymax: 0.8,
        cursorwidth: 7,
      });
    });

  $(".message-toggle").dropdown();
  $(".message-toggle")
    .parent()
    .on("shown.bs.dropdown", function () {
      $(".dropdown-list-message").niceScroll({
        cursoropacitymin: 0.3,
        cursoropacitymax: 0.8,
        cursorwidth: 7,
      });
    });

  if ($(".chat-content").length) {
    $(".chat-content").niceScroll({
      cursoropacitymin: 0.3,
      cursoropacitymax: 0.8,
    });
    $(".chat-content")
      .getNiceScroll(0)
      .doScrollTop($(".chat-content").height());
  }

  if (jQuery().summernote) {
    $(".summernote").summernote({
      dialogsInBody: true,
      minHeight: 250,
    });
    $(".summernote-simple").summernote({
      dialogsInBody: true,
      minHeight: 150,
      toolbar: [
        ["style", ["bold", "italic", "underline", "clear"]],
        ["font", ["strikethrough"]],
        ["para", ["paragraph"]],
      ],
    });
  }

  if (window.CodeMirror) {
    $(".codeeditor").each(function () {
      let editor = CodeMirror.fromTextArea(this, {
        lineNumbers: true,
        theme: "duotone-dark",
        mode: "javascript",
        height: 200,
      });
      editor.setSize("100%", 200);
    });
  }

  // Follow function
  $(".follow-btn, .following-btn").each(function () {
    var me = $(this),
      follow_text = "Follow",
      unfollow_text = "Following";

    me.click(function () {
      if (me.hasClass("following-btn")) {
        me.removeClass("btn-danger");
        me.removeClass("following-btn");
        me.addClass("btn-primary");
        me.html(follow_text);

        eval(me.data("unfollow-action"));
      } else {
        me.removeClass("btn-primary");
        me.addClass("btn-danger");
        me.addClass("following-btn");
        me.html(unfollow_text);

        eval(me.data("follow-action"));
      }
      return false;
    });
  });

  // Dismiss function
  $("[data-dismiss]").each(function () {
    var me = $(this),
      target = me.data("dismiss");

    me.click(function () {
      $(target).fadeOut(function () {
        $(target).remove();
      });
      return false;
    });
  });

  // Collapsable
  $("[data-collapse]").each(function () {
    var me = $(this),
      target = me.data("collapse");

    me.click(function () {
      $(target).collapse("toggle");
      $(target).on("shown.bs.collapse", function (e) {
        e.stopPropagation();
        me.html('<i class="fas fa-minus"></i>');
      });
      $(target).on("hidden.bs.collapse", function (e) {
        e.stopPropagation();
        me.html('<i class="fas fa-plus"></i>');
      });
      return false;
    });
  });

  // Gallery
  $(".gallery .gallery-item").each(function () {
    var me = $(this);

    me.attr("href", me.data("image"));
    me.attr("title", me.data("title"));
    if (me.parent().hasClass("gallery-fw")) {
      me.css({
        height: me.parent().data("item-height"),
      });
      me.find("div").css({
        lineHeight: me.parent().data("item-height") + "px",
      });
    }
    me.css({
      backgroundImage: 'url("' + me.data("image") + '")',
    });
  });
  if (jQuery().Chocolat) {
    $(".gallery").Chocolat({
      className: "gallery",
      imageSelector: ".gallery-item",
    });
  }

  // Background
  $("[data-background]").each(function () {
    var me = $(this);
    me.css({
      backgroundImage: "url(" + me.data("background") + ")",
    });
  });

  // Custom Tab
  $("[data-tab]").each(function () {
    var me = $(this);

    me.click(function () {
      if (!me.hasClass("active")) {
        var tab_group = $('[data-tab-group="' + me.data("tab") + '"]'),
          tab_group_active = $(
            '[data-tab-group="' + me.data("tab") + '"].active'
          ),
          target = $(me.attr("href")),
          links = $('[data-tab="' + me.data("tab") + '"]');

        links.removeClass("active");
        me.addClass("active");
        target.addClass("active");
        tab_group_active.removeClass("active");
      }
      return false;
    });
  });

  // Bootstrap 4 Validation
  $(".needs-validation").submit(function () {
    var form = $(this);
    if (form[0].checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }
    form.addClass("was-validated");
  });

  // alert dismissible
  $(".alert-dismissible").each(function () {
    var me = $(this);

    me.find(".close").click(function () {
      me.alert("close");
    });
  });

  if ($(".main-navbar").length) {
  }

  // Image cropper
  $("[data-crop-image]").each(function (e) {
    $(this).css({
      overflow: "hidden",
      position: "relative",
      height: $(this).data("crop-image"),
    });
  });

  // Slide Toggle
  $("[data-toggle-slide]").click(function () {
    let target = $(this).data("toggle-slide");

    $(target).slideToggle();
    return false;
  });

  // Dismiss modal
  $("[data-dismiss=modal]").click(function () {
    $(this).closest(".modal").modal("hide");

    return false;
  });

  // Width attribute
  $("[data-width]").each(function () {
    $(this).css({
      width: $(this).data("width"),
    });
  });

  // Height attribute
  $("[data-height]").each(function () {
    $(this).css({
      height: $(this).data("height"),
    });
  });

  // Chocolat
  if ($(".chocolat-parent").length && jQuery().Chocolat) {
    $(".chocolat-parent").Chocolat();
  }

  // Sortable card
  if ($(".sortable-card").length && jQuery().sortable) {
    $(".sortable-card").sortable({
      handle: ".card-header",
      opacity: 0.8,
      tolerance: "pointer",
    });
  }

  // Daterangepicker
  if (jQuery().daterangepicker) {
    if ($(".datepicker").length) {
      $(".datepicker").daterangepicker({
        locale: { format: "YYYY-MM-DD" },
        singleDatePicker: true,
      });
    }
    if ($(".datetimepicker").length) {
      $(".datetimepicker").daterangepicker({
        locale: { format: "YYYY-MM-DD HH:mm" },
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
      });
    }
    if ($(".daterange").length) {
      $(".daterange").daterangepicker({
        locale: { format: "YYYY-MM-DD" },
        drops: "down",
        opens: "right",
      });
    }
  }

  // Timepicker
  if (jQuery().timepicker && $(".timepicker").length) {
    $(".timepicker").timepicker({
      icons: {
        up: "fas fa-chevron-up",
        down: "fas fa-chevron-down",
      },
    });
  }
});
