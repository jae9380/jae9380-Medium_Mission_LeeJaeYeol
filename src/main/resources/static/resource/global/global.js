toastr.options={
    closeButton:true,
    newestOnTop:true,
    progressBar:true,
    debug: false,
    positionClass:"toast-top-right",
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing:"swing",
    preventDuplicates:true,
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut",
    closeEasing:"linear",
    opacity: 0.5
}
function parseMsg(msg) {
    const [pureMsg, ttl] = msg.split(";ttl=");

    if ( ttl === undefined ) {
        return [pureMsg, true];
    };

    const currentJsUnixTimestamp = new Date().getTime();

    if (ttl && parseInt(ttl) < currentJsUnixTimestamp) {
        return [pureMsg, false];
    }

    return [pureMsg, true];
}

function parseMsg(msg) {
    const [pureMsg, ttl] = msg.split(";ttl=");

    if ( ttl === undefined ) {
        return [pureMsg, true];
    };

    const currentJsUnixTimestamp = new Date().getTime();

    if (ttl && parseInt(ttl) < currentJsUnixTimestamp) {
        return [pureMsg, false];
    }

    return [pureMsg, true];
}

function toastMsg(isNotice, msg) {
    if (isNotice) toastSuccess(msg);
    else toastWarning(msg);
}

function toastInfo(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["info"](pureMsg, "알림");
    }
}

function toastSuccess(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["success"](pureMsg, "성공");
    }
}

function toastWarning(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["warning"](pureMsg, "경고");
    }
}

function toastError(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["error"](pureMsg, "에러");
    }
}

function getUrlParams(urlString) {
    // 평문 쿼리 문자열을 추출합니다.
    const url = new URL(urlString);
    // URL의 쿼리 문자열 이후로 모든 키와 값을 디코딩합니다.
    const queryParams = new URLSearchParams(url.search);
    // 객체를 만들어 줍니다.
    const params = {};

    for (let [key, value] of queryParams.entries()) {
        // 객체에 키와 값을 추가합니다.
        params[key] = value;
    }

    return params;
}