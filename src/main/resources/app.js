function doWork(jsonStr, queryStr) {
    let json;
    try {
        json = JSON.parse(jsonStr);
    } catch (e) {
        print('Wrong JSON');
        return
    }
    const res = jsqry.query(json, queryStr);
    print(JSON.stringify(res, null, 2));
}