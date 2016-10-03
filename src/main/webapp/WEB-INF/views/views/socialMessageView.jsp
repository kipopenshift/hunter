<%@include file="../common/HeaderFile.jsp"%>






<div id="createNewSocialMessageContainer" class="k-block k-info-colored" style="width: 1000px; margin: 0 auto; padding: 4px; background-color: #d7eaf0; height: 650px; order-radius: 4px; border: 1px solid #afcad2;">
	<div id="processedTaskSocialMsg" style="position:relative;width: 90%; min-height: 480px; color: #0a2c2e; font-size: 13px; padding-left: 10px; background-color: #e6f7ff; margin-left: 4%; margin-top: 30px; border-radius: 4px; border: 1px solid #afcad2;">
		<table style="width:98%;margin-left:1%;table-layout:fixed;height:96%;">
		  <tr>
			<td style="width:100%;min-height:480px;" >

				<table style="width:50%;font-size:14px;" id="createSocialMsgFieldsTable" >
					<tr>
						<td>External ID : </td>
						<td id="creatSclMsgExtrnlId" ></td>
					</tr>
					<tr>
						<td>Media Type : </td>
						<td><input id="createSocialMsgMedaiType" class="k-textbox" style="width:100%;" /></td>
					</tr>
					<tr>
						<td>Description : </td>
						<td><input id="createSocialMsgDescription" class="k-textbox" style="width:100%;background-color:#EBF6FC;" /></td>
					</tr>
					<tr>
						<td>Social Post Type : </td>
						<td><input id="createSocialMsgPostType" class="k-textbox" style="width:100%;" /></td>
					</tr>
				</table>
			</td>
		  </tr>
		  <tr>
			<td style="border-bottom:1px solid #e6f7ff;width:100%;" >
				<table style="width:100%;">
					<tr>
						<td style="border-bottom:1px solid #e6f7ff;width:10.5%;">Social Post</td>
						<td style="border-bottom:1px solid #e6f7ff;width:40%;"><textarea id="createSocialMsgPost" cols="1" rows="3" class="k-textbox" style="width:80%;padding:10px;resize: none;background-color:#EBF6FC;" ></textarea></td>
					</tr>
				</table>
			</td>
		  </tr>
 		  <tr>
			<td style="width:100%;min-height:480px;" >
			  <table style="width:96%;min-height:480px;" >
					<tr style="width:100%;">
						<td style="width:49%;border:1px solid #D8E8F0;height:300px;border-spacing: 2%;border-radius:5px;" >
							<div style="height:80%;width:100%;" >


  
                                    <form id="soclPostUploadFileForms"  method="post" action="/kendo-ui/upload/submit" style="margin-top:10%;width:80%;margin-left:10%;" >
                                        <div class="demo-section k-content">
                                            <input name="createSclMsgUploadPost" id="createSclMsgUploadPost" type="file" />
                                            <p style="text-align: right">
                                            <input style="display:none;" type="submit" value="Upload" class="k-button k-primary" />
                                            </p>
                                        </div>
                                   </form>								


							</div>
						</td>
						<td style="width:2%;" ></td>
						<td style="width:49%;border:1px solid #D8E8F0;height:300px;border-spacing: 2%;border-radius:5px;" ></td>
					</tr>
			  </table>
			</td>
		  </tr>
		  <tr>
			<td style="width:100%;" >
				<table style="width:40%;margin-left:27%;" >
					<tbody>
						<tr>
							<td><button onClick="newTxtMsgManagerInstance.validateAndSubmitTxtMsgData()" class="k-button" style="background-color: #d4eff9; border: 1px solid #78bad2; width: 200px;"><span class="k-icon k-i-tick" ></span>Save</button></td>
							<td><button onClick="newTxtMsgManagerInstance.closeTextMsgWindow()" class="k-button" style="background-color: #d4eff9; border: 1px solid #78bad2; width: 200px;"><span class="k-icon k-i-cancel" ></span>lose</button></td>
						</tr>
					</tbody>
				</table>
			</td>
		  </tr>
		</table>
	</div>
</div>











<%@include file="../common/pageCloser.jsp"%>