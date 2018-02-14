//下面用于图片上传预览功能

			function showdiv() {
				
				document.getElementById("popover-bg").style.display = "block";
				document.getElementById("popover-show").style.display = "block";
				document.getElementById("popover-bg").addEventListener('click', hidediv, false);

			}

			function hidediv() {

				document.getElementById("popover-bg").style.display = 'none';
				document.getElementById("popover-show").style.display = 'none';
			}

			function setImagePreview(avalue) {
				var docObj = document.getElementById("doc");

				var imgObjPreview = document.getElementById("preview");
				if(docObj.files && docObj.files[0]) {
					//火狐下，直接设img属性
					

					//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
					imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
					document.querySelector("#preview-large").src = window.URL.createObjectURL(docObj.files[0]);
					imgObjPreview.addEventListener('click', showdiv, false);
				} else {
					//IE下，使用滤镜
					docObj.select();
					var imgSrc = document.selection.createRange().text;
					var localImagId = document.getElementById("localImag");
					
					//图片异常的捕捉，防止用户修改后缀来伪造图片

					imgObjPreview.addEventListener('click', showdiv, false);
					try {
						localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
						localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
					} catch(e) {
						alert("您上传的图片格式不正确，请重新选择!");
						return false;
					}
					imgObjPreview.style.display = 'none';
					document.selection.empty();
				}
				return true;
			}