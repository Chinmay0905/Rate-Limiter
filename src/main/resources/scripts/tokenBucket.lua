local tokens = redis.call("HGET", KEYS[1], "tokens")
local lastRefillTime = redis.call("HGET", KEYS[1], "lastRefillTime")

local currentTime = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local refillInterval = tonumber(ARGV[3])

-- First request
if not tokens or not lastRefillTime then
    tokens = capacity
    lastRefillTime = currentTime
else
    tokens = tonumber(tokens)
    lastRefillTime = tonumber(lastRefillTime)
end

local elapsedTime = currentTime - lastRefillTime

local newTokens = math.floor(elapsedTime / refillInterval)

if newTokens > 0 then
    tokens = math.min(capacity, tokens + newTokens)
    lastRefillTime = lastRefillTime + (newTokens * refillInterval)
end

if tokens <= 0 then
    redis.call("HSET", KEYS[1], "tokens", tokens)
    redis.call("HSET", KEYS[1], "lastRefillTime", lastRefillTime)
    return 0
end

tokens = tokens - 1

redis.call("HSET", KEYS[1], "tokens", tokens)
redis.call("HSET", KEYS[1], "lastRefillTime", lastRefillTime)

return 1