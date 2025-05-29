/**
 * GET - API 통신
 * @param url
 * @returns {Promise<any|undefined>}
 * @constructor
 */
export const GET = async (url) => {
    return await fetchAction(url, "GET", null);
}

/**
 * POST - API 통신
 * @param url
 * @param data
 * @returns {Promise<any|undefined>}
 * @constructor
 */
export const POST = async (url, data) => {
    return await fetchAction(url, "POST", data);
}

/**
 * POST - API 통신
 * @param url
 * @param data
 * @returns {Promise<any|undefined>}
 * @constructor
 */

export const PUT = async (url, data) => {
    return await fetchAction(url, "PUT", data);
}

export const fetchAction = async (url, method, data) => {
    // const baseUrl = "http://localhost:8080";
    const baseUrl = "https://localhost:8443";

    const options = {
        method: method,
        // headers: {"Content-Type": "application/json"}, @Responsebody
        headers: {"Content-Type": "application/x-www-form-urlencoded"}, // @ModelAttribute, @RequestParam
        credentials: 'include', // 쿠키를 포함하여 요청 보냄
    }

    let resultStatus = "";

    if (typeof data === 'object') {
        // data 형식이 json인 경우
        options.body = (method === "GET") ? undefined : new URLSearchParams(data); // GET은 body 없음
    } else {
        // 그 외
        options.body = new URLSearchParams(data);
    }

    try {

        const response = await fetch(`${baseUrl}${url}`, options);

        if (!response.ok) {
            resultStatus = response.status;
            throw new Error(response.statusText);
        }

        return await response.json();

    } catch (e) {
        alert(resultStatus);
    }

}