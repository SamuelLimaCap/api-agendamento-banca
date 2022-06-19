package com.gru.ifsp.AgendamentoBanca.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailTemplate {

    public String title;
    public String description;
    public String content;
    public String link;
    public String buttonTitle;

    @Override
    public String toString() {
        return "<!doctype html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <title>Simple Transactional Email</title>\n" +
                "    <style>\n" +
                "@media only screen and (max-width: 620px) {\n" +
                "  table.body h1 {\n" +
                "    font-size: 28px !important;\n" +
                "    margin-bottom: 10px !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body p,\n" +
                "table.body ul,\n" +
                "table.body ol,\n" +
                "table.body td,\n" +
                "table.body span,\n" +
                "table.body a {\n" +
                "    font-size: 16px !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body .wrapper,\n" +
                "table.body .article {\n" +
                "    padding: 10px !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body .content {\n" +
                "    padding: 0 !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body .container {\n" +
                "    padding: 0 !important;\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body .main {\n" +
                "    border-left-width: 0 !important;\n" +
                "    border-radius: 0 !important;\n" +
                "    border-right-width: 0 !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body .btn table {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body .btn a {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "\n" +
                "  table.body .img-responsive {\n" +
                "    height: auto !important;\n" +
                "    max-width: 100% !important;\n" +
                "    width: auto !important;\n" +
                "  }\n" +
                "}\n" +
                "@media all {\n" +
                "  .ExternalClass {\n" +
                "    width: 100%;\n" +
                "  }\n" +
                "\n" +
                "  .ExternalClass,\n" +
                ".ExternalClass p,\n" +
                ".ExternalClass span,\n" +
                ".ExternalClass font,\n" +
                ".ExternalClass td,\n" +
                ".ExternalClass div {\n" +
                "    line-height: 100%;\n" +
                "  }\n" +
                "\n" +
                "  .apple-link a {\n" +
                "    color: inherit !important;\n" +
                "    font-family: inherit !important;\n" +
                "    font-size: inherit !important;\n" +
                "    font-weight: inherit !important;\n" +
                "    line-height: inherit !important;\n" +
                "    text-decoration: none !important;\n" +
                "  }\n" +
                "\n" +
                "  #MessageViewBody a {\n" +
                "    color: inherit;\n" +
                "    text-decoration: none;\n" +
                "    font-size: inherit;\n" +
                "    font-family: inherit;\n" +
                "    font-weight: inherit;\n" +
                "    line-height: inherit;\n" +
                "  }\n" +
                "\n" +
                "  .btn-primary table td:hover {\n" +
                "    background-color: #34495e !important;\n" +
                "  }\n" +
                "\n" +
                "  .btn-primary a:hover {\n" +
                "    background-color: #34495e !important;\n" +
                "    border-color: #34495e !important;\n" +
                "  }\n" +
                "}\n" +
                "</style>\n" +
                "  </head>\n" +
                "  <body style=\"background-color: #f6f6f6; font-family: sans-serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "    <span class=\"preheader\" style=\"color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0;\">"+title+".</span>\n" +
                "    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #f6f6f6; width: 100%;\" width=\"100%\" bgcolor=\"#f6f6f6\">\n" +
                "      <tr>\n" +
                "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\" valign=\"top\">&nbsp;</td>\n" +
                "        <td class=\"container\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; display: block; max-width: 580px; padding: 10px; width: 580px; margin: 0 auto;\" width=\"580\" valign=\"top\">\n" +
                "          <div class=\"content\" style=\"box-sizing: border-box; display: block; margin: 0 auto; max-width: 580px; padding: 10px;\">\n" +
                "\n" +
                "            <!-- START CENTERED WHITE CONTAINER -->\n" +
                "            <table role=\"presentation\" class=\"main\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background: #ffffff; border-radius: 3px; width: 100%;\" width=\"100%\">\n" +
                "\n" +
                "              <!-- START MAIN CONTENT AREA -->\n" +
                "              <tr>\n" +
                "                <td class=\"wrapper\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; box-sizing: border-box; padding: 20px;\" valign=\"top\">\n" +
                "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\" width=\"100%\">\n" +
                "                    <tr>\n" +
                "                      <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\" valign=\"top\">\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px;\">Ola,</p>\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px;\">"+description+".</p>\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px;\">"+content+".</p>\n" +
                "                        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; box-sizing: border-box; width: 100%;\" width=\"100%\">\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td align=\"left\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; padding-bottom: 15px;\" valign=\"top\">\n" +
                "                                <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: auto;\">\n" +
                "                                  <tbody>\n" +
                "                                    <tr>\n" +
                "                                      <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; border-radius: 5px; text-align: center; background-color: #3498db;\" valign=\"top\" align=\"center\" bgcolor=\"#3498db\"> <a href=\""+link+"\" target=\"_blank\" style=\"border: solid 1px #3498db; border-radius: 5px; box-sizing: border-box; cursor: pointer; display: inline-block; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; text-decoration: none; text-transform: capitalize; background-color: #3498db; border-color: #3498db; color: #ffffff;\">"+buttonTitle+"</a> </td>\n" +
                "                                    </tr>\n" +
                "                                  </tbody>\n" +
                "                                </table>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px;\">IFSP guarulhos.</p>\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px;\">Gerenciamento de bancas.</p>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "\n" +
                "            <!-- END MAIN CONTENT AREA -->\n" +
                "            </table>\n" +
                "            <!-- END CENTERED WHITE CONTAINER -->\n" +
                "\n" +
                "            <!-- START FOOTER -->\n" +
                "            <div class=\"footer\" style=\"clear: both; margin-top: 10px; text-align: center; width: 100%;\">\n" +
                "              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\" width=\"100%\">\n" +
                "                <tr>\n" +
                "                  <td class=\"content-block\" style=\"font-family: sans-serif; vertical-align: top; padding-bottom: 10px; padding-top: 10px; color: #999999; font-size: 12px; text-align: center;\" valign=\"top\" align=\"center\">\n" +
                "                    <span class=\"apple-link\" style=\"color: #999999; font-size: 12px; text-align: center;\">Company Inc, 3 Abbey Road, San Francisco CA 94102</span>\n" +
                "                    <br> Don't like these emails? <a href=\"http://i.imgur.com/CScmqnj.gif\" style=\"text-decoration: underline; color: #999999; font-size: 12px; text-align: center;\">Unsubscribe</a>.\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                  <td class=\"content-block powered-by\" style=\"font-family: sans-serif; vertical-align: top; padding-bottom: 10px; padding-top: 10px; color: #999999; font-size: 12px; text-align: center;\" valign=\"top\" align=\"center\">\n" +
                "                    Powered by <a href=\"http://htmlemail.io\" style=\"color: #999999; font-size: 12px; text-align: center; text-decoration: none;\">HTMLemail</a>.\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </table>\n" +
                "            </div>\n" +
                "            <!-- END FOOTER -->\n" +
                "\n" +
                "          </div>\n" +
                "        </td>\n" +
                "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\" valign=\"top\">&nbsp;</td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>";
    }
}
