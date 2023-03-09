const CLIENT_ID = "main-server-client";
const SCOPE = "openid";
const RESPONSE_TYPE_CODE = "code";


const GRANT_TYPE_AUTH_CODE = "authorization_code";
const GRANT_TYPE_REFRESH_TOKEN = "refresh_token";


const SHA_256 = "SHA-256"
const S256 = "S256";

const KEYCLOAK_URI = "http://localhost:8085/realms/ewm-realm/protocol/openid-connect";

const CLIENT_ROOT_URL = "http://localhost:8180/";

const RESOURCE_SERVER_URL = "http://localhost:8080/";

let accessToken = '';
let refreshToken = '';
let idToken = '';


const ID_TOKEN_KEY = "IT";
const STATE_KEY = "ST";
const CODE_VERIFIER_KEY = "CV";
const REFRESH_TOKEN_KEY = "RT";


function initPage() {

    refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY);

    if (refreshToken) {
        exchangeRefreshToAccessToken();
    } else {
        if (!checkAuthCode()) {
            initAccessToken();
        }
    }
}

function initAccessToken() {
    var state = generateState(30);
    localStorage.setItem(STATE_KEY, state);

    var codeVerifier = generateCodeVerifier();
    localStorage.setItem(CODE_VERIFIER_KEY, codeVerifier);

    generateCodeChallenge(codeVerifier).then(codeChallenge => {
        requestAuthCode(state, codeChallenge)
    });
}

function checkAuthCode() {
    var urlParams = new URLSearchParams(window.location.search);
    var authCode = urlParams.get('code');
    var state = urlParams.get('state');

    if (!authCode) {
        return false;
    }

    requestTokens(state, authCode)

    return true;
}


function generateCodeVerifier() {
    var randomByteArray = new Uint8Array(43);
    window.crypto.getRandomValues(randomByteArray);
    return base64urlEncode(randomByteArray);
}

function base64urlEncode(sourceValue) {
    return btoa(String.fromCharCode.apply(null, sourceValue))
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=/g, '');
}

function generateState(length) {
    var state = "";
    var alphaNumericCharacters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (var i = 0; i < alphaNumericCharacters.length; i++) {
        state += alphaNumericCharacters.charAt(Math.floor(Math.random() * alphaNumericCharacters.length));
    }
    return state;
}

async function generateCodeChallenge(codeVerifier) {
    var digest = await window.crypto.subtle.digest(SHA_256, new TextEncoder('US-ASCII').encode(codeVerifier));
    return base64urlEncode(Array.from(new Uint8Array(digest)));
}

function requestAuthCode(state, codeChallenge) {
    var authUrl = KEYCLOAK_URI + "/auth";

    authUrl += "?response_type=" + RESPONSE_TYPE_CODE;
    authUrl += "&client_id=" + CLIENT_ID;
    authUrl += "&state=" + state;
    authUrl += "&scope=" + SCOPE;
    authUrl += "&code_challenge=" + codeChallenge;
    authUrl += "&code_challenge_method=" + S256;
    authUrl += "&redirect_uri=" + CLIENT_ROOT_URL;

    window.open(authUrl, '_self');
}

function requestTokens(stateFromAuthServer, authCode) {
    console.log("requestToken");

    var originalState = localStorage.getItem(STATE_KEY);

    if (stateFromAuthServer === originalState) {

        var codeVerifier = localStorage.getItem(CODE_VERIFIER_KEY);

        var data = {
            "grant_type": GRANT_TYPE_AUTH_CODE,
            "client_id": CLIENT_ID,
            "code": authCode,
            "code_verifier": codeVerifier,
            "redirect_uri": CLIENT_ROOT_URL
        };

        $.ajax({
            beforeSend: function (request) {
                request.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            },
            type: "POST",
            url: KEYCLOAK_URI + "/token",
            data: data,
            success: accessTokenResponse,
            dataType: "json"
        });
    } else {
        initAccessToken();
    }
}

function accessTokenResponse(data, status, jqXHR) {

    localStorage.removeItem(STATE_KEY);
    localStorage.removeItem(CODE_VERIFIER_KEY);

    accessToken = data["access_token"];
    refreshToken = data["refresh_token"];
    idToken = data["id_token"];

    console.log("access_token = " + accessToken);
    console.log("refresh_token = " + refreshToken);

    localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
    localStorage.setItem(ID_TOKEN_KEY, idToken);

    getDataFromResourceServer();
}

function getDataFromResourceServer() {
    console.log("getDataFromResourceServer")

    var payloadData = atob(accessToken.split(".")[1]);
    var claimSub = JSON.parse(payloadData)["sub"];
    var userId = claimSub.split(":")[2];
    console.log("userId from jwt " + userId)

    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            request.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
        type: "GET",
        url: RESOURCE_SERVER_URL + "users/" + userId + "/events",
        success: resourceServerResponse,
        error: resourceServerError,
        dataType: "json"
    });
}

function resourceServerResponse(data, status, jqXHR) {
    let json = JSON.stringify(data);

    console.log("resourceServerResponse Ok");

    document.getElementById("userdata").innerHTML = json;
}

function resourceServerError(request, status, error) {
    console.log("resourceServerError");

    var json = JSON.parse(request.responseText);
    var ex = json["errors"][0];
    var message = json["message"];

    console.log("exception" + ex);

    refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY);

    if (ex === 'OAuth2AuthenticationException') {
        initAccessToken();
    } else if (ex === 'InvalidBearerTokenException') {
        exchangeRefreshToAccessToken();
    } else {
        document.getElementById("userdata").innerHTML = "error " + message;
    }
}

function exchangeRefreshToAccessToken() {

    console.log("new access token initiated");

    var data = {
        "grant_type": GRANT_TYPE_REFRESH_TOKEN,
        "client_id": CLIENT_ID,
        "refresh_token": refreshToken
    };

    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        },
        type: "POST",
        url: KEYCLOAK_URI + "/token",
        data: data,
        success: accessTokenResponse,
        error: exchangeRefreshError,
        dataType: "json"
    });
}

function exchangeRefreshError(request, status, error) {
    logout();
}

function login() {
    if (refreshToken) {
        exchangeRefreshToAccessToken();
    } else {
        initAccessToken();
    }

    getDataFromResourceServer();
}

function logout() {

    var idToken = localStorage.getItem(ID_TOKEN_KEY);

    console.log("logout");

    var authUrl = KEYCLOAK_URI + "/logout";

    authUrl += "?post_logout_redirect_uri=" + CLIENT_ROOT_URL;
    authUrl += "&id_token_hint=" + idToken;
    authUrl += "&client_id=" + CLIENT_ID;


    window.open(authUrl, '_self');

    localStorage.removeItem(REFRESH_TOKEN_KEY);
    localStorage.removeItem(ID_TOKEN_KEY);

    accessToken = "";
    refreshToken = "";
}
