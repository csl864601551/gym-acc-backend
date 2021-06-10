local initIndex = tonumber(1000000000)
local increment = tonumber(ARGV[1])
local codeType = tonumber(ARGV[2])

local exist = redis.call("EXISTS", KEYS[1])
 if exist == 0 then
    if codeType == 0 then
        local after = initIndex+increment;
        local add = redis.call("SET", KEYS[1], after);
        return initIndex..after;
    end
    if codeType == 1 then
       local after = initIndex+increment+1;
       local add = redis.call("SET", KEYS[1], after);
       return initIndex..after;
    end
 end
 if exist == 1 then
    if codeType == 0 then
        local before = redis.call("GET", KEYS[1]);
        local after = before+increment;
        local add = redis.call("SET", KEYS[1], after);
        return before..'-'..after;
    end
    if codeType == 1 then
        local before = redis.call("GET", KEYS[1]);
        local after = before+increment+1;
        local add = redis.call("SET", KEYS[1], after);
        return before..'-'..after;
    end
 end
