
ip,port,temp_data_pin,temp_pwr_pin,sleep_time=...

if(ip==nil) then print('data is nil!') return end


print("main")

local timer = tmr.create();
timer:register(500, tmr.ALARM_AUTO, function (t)
	q,t,h,tt,hh = dht.read11(temp_data_pin)
	if(q==0) then
		print("temp data: "..q.." "..t.." "..h.." "..tt.." "..hh) 
		timer:unregister()
		if (wifi.sta.status()==wifi.STA_GOTIP) then
			print("ip: "..wifi.sta.getip())
			send_data(t,h,tt,hh)
		else
		wifi.eventmon.register(wifi.eventmon.STA_GOT_IP, function(T)
			print("ip: "..T.IP)
			send_data(t,h,tt,hh)
			end)		
		end
	end
end)
timer:start()

function send_data(t,h,tt,hh)
    was_sent=0
	srv = net.createConnection(net.TCP, 0)
	srv:on("connection", function(sck, c)
        data = "T="..t.."."..tt..";H="..h.."."..hh;
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
