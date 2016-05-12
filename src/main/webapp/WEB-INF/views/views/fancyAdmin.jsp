<%@include file="headerFancyFile.jsp"%>



	<div id="fancyWeather">
		
		<div id="fancyWeatherTodayDetails">
			
			<div style="margin: 0 auto; width:90%;padding-top:2%;text-align: center;">
				<table style="width:100%">
					<tr>
						<td style="font-size:25px; font-weight:bold;" id="barakaWeatherLocation" >Princeton, New Jersey, United States</td>
					</tr>
					<tr>
						<td>
							<table  style="margin:0 auto;width:50%">
								<tr>
									<td style="border-right:1px solid #D7D9F5"><span id="todayDayMonthAndDate" >Monday Jan 23</span><sup><span id="todayDateSuperscript">rd</span></sup> <span id="todayDateYear">2015</span> </td>
									<td><img id="refreshBarakaWeatherIcon" style="border-radius:3px;margin-auto:0 auto;" alt="No Image" src="<c:url value='/static/resources/gfx/refresh_baraka_weather_icon.png'/>" width="18px" height="18px" /></td>
								</tr>
								<tr>
									<td style="border-right:1px solid #D7D9F5"  id="todayTimeRightNow" >22:29:55 PM</td>
									<td><span id="refreshText">Refresh</span> </td>
								</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<table style="width:30%;margin-left:33%;">
								<tr>
									<td style="font-size:20px;" id="todayWeatherStatusDescription" >Sunny, Fair</td>
									<td id="todayWeatherStatusIcon"><img style="border-radius:3px;" alt="No Image" src="<c:url value='/static/resources/gfx/sunny.jpg'/>" width="25px" height="25px" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
			
			
			
			<table style="margin:0 auto;width:38%;border:0;color:white;text-align: center;position:relative;">
				<tr style="width:100%;">
					<td  style="width:100%;">
						<table  style="width:100%;">
							<tr>
								<td style="font-size:18px;font-weight: bold;text-align: center;">Low</td>
								<td style="font-size:18px;font-weight: bold;text-align: center;"><span id="todayTempRightNow" >40</span> <sup>o</sup><span class="tempMeasurement">F</span></td>
								<td style="font-size:18px;font-weight: bold;text-align: center;" >High</td>
							</tr>
							<tr>
								<td style="font-size:15px; text-align: center;" ><span id="todayTempLow" >15</span> <sup>o</sup><span class="tempMeasurement">F</span></td>
								<td style="font-size:13px;text-align: center;" >Real Feel : <span id="todayTempRealFeel">20</span><sup>o</sup><span class="tempMeasurement">F</span></td>
								<td style="font-size:15px; text-align: center;" ><span id="todayTempHigh">20</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			
		</div>
		
		
		<div id="fancyWeatherFocusDetails">
			<table  style="width:100%;border:0;color:white;text-align: center;margin:0 auto;padding-top:1px;">
				
				
				<tr  style="font-weight: bold;font-size:20px;">
					<td><span id="dayAfter1NameAndDate">Tuesday 27</span><sup><span id="dayAfter1NameAndDateSuperscript"></span></sup></td>
					<td><span id="dayAfter2NameAndDate">Wednesday 28</span><sup id="dayAfter2NameAndDateSuperscript">th</sup></td>
					<td><span id="dayAfter3NameAndDate">Thursday 29</span><sup id="dayAfter3NameAndDateSuperscript">th</sup></td>
					<td><span id="dayAfter4NameAndDate">Friday 30</span><sup id="dayAfter4NameAndDateSuperscript">th</sup></td>
				</tr>
				
				<tr  style="font-size:20px;">
					<td style="border-right:1px solid white;">
						<table style="width:100%;border:0;color:white;text-align:center;margin:0 auto;">
							<tr style="text-align:center;font-size:17px;">
								<td>High <span id="dayAfter1High">51</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
							<tr style="text-align:center;font-size:17px;">
								<td>Low <span id="dayAfter1Low">25</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
						</table>
					</td>
					<td style="border-right:1px solid white;">
						<table style="width:100%;border:0;color:white;text-align: center;margin:0 auto;">
							<tr style="text-align:center;font-size:17px;">
								<td>High <span id="dayAfter2High">58</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
							<tr style="text-align:center;font-size:17px;">
								<td>Low <span id="dayAfter2Low">30</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
						</table>
					</td>
					<td style="border-right:1px solid white;">
						<table style="width:100%;border:0;color:white;text-align: center;margin:0 auto;">
							<tr style="text-align:center;font-size:17px;">
								<td>High <span id="dayAfter3High">60</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
							<tr style="text-align:center;font-size:17px;">
								<td>Low <span id="dayAfter3Low">35</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
						</table>
					</td>
					<td>
						<table style="width:100%;border:0;color:white;text-align: center;margin:0 auto;">
							<tr style="text-align:center;font-size:17px;">
								<td>High <span id="dayAfter4High">62</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
							<tr style="text-align:center;font-size:17px;">
								<td>Low <span id="dayAfter4Low">40</span><sup>o</sup><span class="tempMeasurement">F</span></td>
							</tr>
						</table>
					</td>
				</tr>
				
				
				
				
				
				<tr   style="font-size:20px;">
					<td>
						<table style="width:100%;border:0;color:white;margin:0 auto;">
							<tr style="font-size:17px;width:50%;">
								<td id="dayAfter1IconTd" ><img style="border-radius:3px;" alt="No Image" src="<c:url value='/static/resources/gfx/sunny.jpg'/>" width="40px" height="40px" /></td>
							</tr>
							<tr style="text-align:center;font-size:17px;width:50%;">
								<td id="dayAfter1Description" >Sunny</td>
							</tr>
						</table>
					</td>
					<td>
						<table style="width:100%;border:0;color:white;margin:0 auto;">
							<tr style="font-size:17px;width:50%;">
								<td id="dayAfter2IconTd" ><img style="border-radius:3px;margin-left:5%;" alt="No Image" src="<c:url value='/static/resources/gfx/cloudy_moderate.jpg'/>" width="40px" height="40px" /></td>
							</tr>
							<tr style="text-align:center;font-size:17px;width:50%;">
								<td id="dayAfter2Description" >Cloudy</td>
							</tr>
						</table>
					</td>
					<td>
						<table style="width:100%;border:0;color:white;margin:0 auto;">
							<tr style="font-size:17px;width:50%;">
								<td id="dayAfter3IconTd" ><img style="border-radius:2px;margin-left:5%;" alt="No Image" src="<c:url value='/static/resources/gfx/thunderstorm.jpg'/>" width="40px" height="40px" /></td>
							</tr>
							<tr style="text-align:center;font-size:17px;width:50%;">
								<td id="dayAfter3Description" >Thunderstorm</td>
							</tr>
						</table>
					</td>
					<td>
						<table style="width:100%;border:0;color:white;margin:0 auto;">
							<tr style="font-size:17px;width:50%;">
								<td id="dayAfter4IconTd" ><img style="border-radius:3px;margin-left:5%;" alt="No Image" src="<c:url value='/static/resources/gfx/rainy_showers.jpg'/>" width="40px" height="40px" /></td>
							</tr>
							<tr style="font-size:17px;width:50%;">
								<td id="dayAfter4Description" >Showers</td>
							</tr>
						</table>
					</td>	
				</tr>
				
				
				
				
				
				
			</table>
		</div>
	</div>
	
	<div id="searchResultFancyHolder" class="hidden">
		
	</div>
	
	<div id="videos">
		<a class="fancybox fancybox.iframe" href="http://www.youtube.com/embed/L9szn1QQfas?enablejsapi=1&wmode=opaque">Video #1</a><br />
		<a class="fancybox fancybox.iframe" href="http://www.youtube.com/embed/cYplvwBvGA4?enablejsapi=1&wmode=opaque">Video #2</a>
	</div>

	<div id="contents">
		<a class="fancybox fancybox.ajax" href="http://localhost:8080/Baraka/search/match/str/*">Get All Goods</a>
		<a class="fancybox-media" href="https://www.youtube.com/watch?v=SwAQox2ONDM">Youtube</a>
		<a href="http://localhost:8080/Baraka/chat/goToChat">Chat</a>
		<a href="http://localhost:8080/Baraka/chat/login">Chat Login</a>
		<a class="fancybox fancybox.ajax" href="http://localhost:8080/Baraka/search/match/str/*">Get All Goods</a>
	</div>



	</div>

</body>