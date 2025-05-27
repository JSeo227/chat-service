/**
 * GET - API 통신
 * @param {*} url
 */
export const GET = async (url) => {
    return await fetchAction(url, "GET", null);
}

/**
 * POST - API 통신
 * @param {*} url
 * @param {*} data
 */
export const POST = async (url, data) => {
    return await fetchAction(url, "POST", data);
}

export const fetchAction = async (url, method, data, isFileUpload = false) => {
    const baseUrl = "http://localhost:8080";

    const options = {
        method: method,
        headers: {"Content-Type": "application/json"},
        credentials: 'include', // 쿠키를 포함하여 요청 보냄
    }

    let resultStatus = "";

    if (typeof data === 'object') {
        // data 형식이 json인 경우
        options.body = (method === "GET") ? undefined : JSON.stringify(data); // GET은 body 없음
    } else {
        // 그 외
        options.body = data;
    }

    try {

        const response = await fetch(`${baseUrl}${url}`, options);

        if (!response.ok) {
            resultStatus = response.status;
            throw new Error(response.statusText);
        }

        const result = await response.json();

        return result;

    } catch (e) {
        alert(resultStatus + " : " + e.message);
    }

}