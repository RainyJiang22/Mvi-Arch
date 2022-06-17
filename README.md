# Mvi-Arch

Pursue better practice of android architecture

## MVI Feature

- 相对于MVVM，强调是数据单向流动，很容易进行数据比对与回溯
- 使用ViewState对State进行集中管理，只需要订阅一个`ViewState`就可以获取页面所有状态，减少了很多模板代码
- 通过viewState与Action进行通信，浏览`viewstate`与`action`就可以清楚知道`viewModel`的职责
- 如果用MVI代替没有使用`databinding`的MVVM是一个比较好的选择

