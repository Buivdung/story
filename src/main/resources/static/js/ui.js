function getChapter() {
    window.location.href = href + chapterId.val();
}

function getChapterPrev() {
    window.location.href = href + $("#idPrev").val();
}

function getChapterNext() {
    window.location.href = href + $("#idNext").val();
}

let chapterId = $("#chapterId").on("change", getChapter);
let href = "http://localhost:8080/" +  $("#storyId").val() + "/chapter/";
$(".chapterIdPrev").on("click", getChapterPrev);
$(".chapterIdNext").on("click", getChapterNext);


let msg = new SpeechSynthesisUtterance;
let content = $(".content");
const stop = () => {
    window.speechSynthesis.cancel();
    playPause.html(`<i class="fa-solid fa-play h1"></i>
            <H6>Play</H6>`);
}

const play = () => {

    if (!window.speechSynthesis.speaking) {
        let contextSelected = document.getSelection().toString();
        msg.lang = "vi-VN"
        msg.text = contextSelected === "" ? content.text() : contextSelected;
        window.speechSynthesis.speak(msg);
        playPause.html(`<i class="fa-solid fa-pause h1"></i>
            <H6>Pause</H6>`);
    } else if (!window.speechSynthesis.paused) {
        window.speechSynthesis.pause();
        playPause.html(`<i class="fa-solid fa-play h1"></i>
            <H6>Play</H6>`);
    } else {
        window.speechSynthesis.resume();
        playPause.html(`<i class="fa-solid fa-pause h1"></i>
            <H6>Pause</H6>`);
    }
}

let playPause = $("#play-pause").on("click", play);
$("#stopVoid").on("click", stop);
window.onbeforeunload = () => {
    stop();
}

// comment

function postCmt(e) {
    e.preventDefault();
    let commentReq = $("#comment").serialize();
    $.post("http://localhost:8080/comment", commentReq, "json").always(function () {
        window.location.reload();
    })
    $("#textCmt").val("");
}

function focusI() {
    for (let i = 0; i < autoFocus.length; i++) {
        if ($(autoFocus[i]).val() === "") {
            $(autoFocus[i]).focus();
            return
        }
    }
}

function payment(id) {
    $.get("http://localhost:8080/create_pay/" + id).done(function (data) {
        window.location.href = data;
    })
}

let autoFocus = $(".autoFocus").on("input", focusI);
$("#postComment").on("click", postCmt);

