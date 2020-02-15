
ip,port,temp_data_pin,temp_pwr_pin,sleep_time=...

if(ip==nil) then print('data is nil!') return end


print("main")

alt=0 -- altitude of the measurement place

sda, scl = 4,3
i2c.setup(0, sda, scl, i2c.SLOW) -- call i2c.setup() only once

bme280.setup()

local timer = tmr.create();
timer:register(500, tmr.ALARM_AUTO, function (t)

	T, P, H, QNH = bme280.read(alt)


	if(P>0) then
		print("temp data: "..T.." "..P.." "..H.." "..QNH) 
		timer:unregister()
		if (wifi.sta.status()==wifi.STA_GOTIP) then
			print("ip: "..wifi.sta.getip())
			send_data(T,P,H,QNH)
		else
		wifi.eventmon.register(wifi.eventmon.STA_GOT_IP, function(TT)
			print("ip: "..TT.IP)
			send_data(T,P,H,QNH)
			end)		
		end
	end
end)
timer:start()

function send_data(T,P,H,QNH)
    was_sent=0
	srv = net.createConnection(net.TCP, 0)
	srv:on("connection", function(sck, c)
        data = "BME1: "..T.." "..P.." "..H.." "..QNH;
        print("trying to send: "..data)
		sck:send(data,function(sck,c)
            print("data was sent, going to sleep")
            was_sent=1
            sck:close()
            end)
		end)
	srv:connect(port,ip)
    timer:register(3000,tmr.ALARM_AUTO, function(t)
        if(was_sent==0) then srv:connect(port,ip)
        timer:unregister()

        end
    end)
    timer:start()
end
