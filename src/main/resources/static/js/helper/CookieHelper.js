const CookieHelper = {
    createCookie: (name, value, path) => {
        const maxAge = 60 * 60 * 24 * 365;
        document.cookie = `${name} = ${value}; max-age = ${maxAge}; path = ${path}`;
    }
};