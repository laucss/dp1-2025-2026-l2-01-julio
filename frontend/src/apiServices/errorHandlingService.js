const statusCodeHandler = (resEndpoint, entity, message, setMessage) => {
  const status = resEndpoint.status;
  let isError = status >= 400 && status < 600;
  if (isError && (message === null || (typeof message === 'string' && message?.includes(entity)))) {
    const resMessage = resEndpoint.data?.message;
    if (status >= 500 && status < 600) {
      setMessage(`Unknown server error on entity ${entity}: ${status}`);
    } else if (resMessage) {
      setMessage(resMessage);
    } else if (status === 404) {
      setMessage(`${entity} has been deleted or was not found`);
    } else if (status >= 400 && status < 500) {
      setMessage(`Unknown client error on entity ${entity}: ${status}`);
    } else {
      setMessage(null);
    }
  }
  return isError;
};

const serviceResponseHandler = (resEndpoint, entity, message, setMessage) => {
  const isError = statusCodeHandler(resEndpoint, entity, message, setMessage);
  if (isError) {
    return null;
  }
  return resEndpoint.data;
};

const serviceErrorHandler = async (functionToValidate) => {
  let res = null;
  try {
    res = await functionToValidate();
  } catch (err) {
    res = { status: 500, data: null };
  }
  return res;
};

export { statusCodeHandler, serviceResponseHandler, serviceErrorHandler };
