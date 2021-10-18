function translate(values) {
  values = JSON.parse(values);
  values.notificationStatus = values.notificationStatus.toString();
  return JSON.stringify(values);
}
