// function search(pageNumber) {
//     var searchUserReq = {
//         "pageNumber": 1,
//         "pageSize":$("#pageSize").val(),
//         "value":$("#search").val()
//     }
//     $.ajax({
//         type:"GET",
//         data: searchUserReq,
//         contentType: "application/json",
//         url:    "http://localhost:8080/admin/um/accounts"});
// }
//


$(document).ready(function () {
    setTimeout(function hideAlert() {
        $("#alertCustom").hide();
    }, 3000);


    function auto() {
        btnManual.removeClass("btn-secondary");
        btnAuto.addClass("btn-success");
        btnAuto.css("color", "white")
        btnManual.css("color", "gray");
        let id = $("#storyId").text();
        $.get("http://localhost:8080/admin/chapter/total/" + id).done(function (count) {
            numberChapter.prop("readonly", true);
            numberChapter.val(count);
        })
    }

    function manual() {
        btnAuto.removeClass("btn-success");
        btnAuto.css("color", "green");
        btnManual.addClass("btn-secondary");
        btnManual.css("color", "white");
        numberChapter.prop("readonly", false);
        numberChapter.val("");
    }


    function getChapter() {
        let href = window.location.href.slice(0, 38);
        window.location.href = href + chapterId.val();
    }

    function getChapterPrev() {
        let href = window.location.href.slice(0, 38);
        window.location.href = href + $("#idPrev").val();
    }

    function getChapterNext() {
        let href = window.location.href.slice(0, 38);
        window.location.href = href + $("#idNext").val();
    }

    let btnAuto = $("#auto").on("click", auto);
    let btnManual = $("#manual").on("click", manual);
    let numberChapter = $("#number");
    let chapterId = $("#chapterId").on("change", getChapter);
    $(".chapterIdPrev").on("click", getChapterPrev);
    $(".chapterIdNext").on("click", getChapterNext);
})

function closeEdit() {
    editThumbnailBtn.hide();
    editIntroductionBtn.hide();
    editContentBtn.hide();
}

function editThumbnail() {
    editThumbnailBtn.show();
}

function editContent() {
    editContentBtn.show();
}

function editIntroduction() {
    editIntroductionBtn.show();
}



function deleteById(id) {
    let deleteBtn = $("#delete-button").empty();
    let entity = event.target.getAttribute("id");
    deleteBtn.empty();
    let href = "http://localhost:8080/admin/" + entity + "/delete/" + id;
    deleteBtn.append(`
        <div class="d-flex justify-content-center py-3">
        <button type="button" class="btn btn-secondary me-3" data-bs-dismiss="modal">Cancel</button>
        <a href="${href}" role="button" class="btn btn-primary">Delete</a>
        </div>
    `);
}

function editGenreById(id) {
    $.get("http://localhost:8080/admin/genre/" + id).done(
        function (data) {
            $("#id").val(data.id);
            $("#name").val(data.name);
        }
    ).fail(
        function (err) {

        }
    )
}

let editThumbnailBtn = $("#editThumbnail").hide();

let editIntroductionBtn = $("#editIntroduction").hide();
let editContentBtn = $("#editContent").hide();

