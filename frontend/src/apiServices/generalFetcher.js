async function generalFetcher(url, jwt, method = "GET", body = null) {
  const res = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + jwt,
    },
    method: method,
    body: body ? JSON.stringify(body) : null,
  });
  const data = await res.json();
  return { status: res.status, data };
}

export { generalFetcher };